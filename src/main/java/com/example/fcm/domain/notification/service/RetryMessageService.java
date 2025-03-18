package com.example.fcm.domain.notification.service;

import com.example.fcm.domain.device.service.DeviceService;
import com.example.fcm.domain.metrics.service.PushMetricsService;
import com.example.fcm.domain.notification.entity.MessageStatus;
import com.example.fcm.domain.notification.entity.PushMessage;
import com.example.fcm.domain.notification.repository.PushMessageRepository;
import com.example.fcm.infra.fcm.FcmService;
import com.example.fcm.infra.redis.RedisTemplateManager;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MessagingErrorCode;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RetryMessageService {

    private final PushMessageRepository pushMessageRepository;
    private final DeviceService deviceService;
    private final FcmService fcmService;
    private final RedisTemplateManager redisTemplateManager;
    private final PushMetricsService pushMetricsService;

    private static final int MAX_RETRY_ATTEMPTS = 5;

    /**
     * 메시지 ID로 재시도 처리
     */
    @Transactional
    public void processRetryById(Long messageId) {
        try {
            // 재시도 메트릭 증가
            pushMetricsService.incrementRetryAttempts();

            // 메시지 조회
            Optional<PushMessage> messageOpt = pushMessageRepository.findById(messageId);

            if (messageOpt.isEmpty()) {
                log.warn("재시도할 메시지가 존재하지 않음: ID={}", messageId);
                return;
            }

            PushMessage message = messageOpt.get();

            // 상태 확인
            if (message.getStatus() != MessageStatus.PENDING) {
                log.warn("재시도 무시 - 상태가 PENDING이 아님: ID={}, 상태={}",
                        messageId, message.getStatus());
                return;
            }

            // 처리 중으로 상태 변경 및 재시도 횟수 증가
            message.setStatus(MessageStatus.IN_PROGRESS);
            message.setRetryCount(message.getRetryCount() + 1);
            message.setLastAttemptTime(LocalDateTime.now());
            pushMessageRepository.save(message);

            // 부모 ID로 최신 FCM 토큰 조회
            String fcmToken = deviceService.getLatestFcmTokenByParentId(message.getParentId());

            if (!StringUtils.hasText(fcmToken)) {
                log.error("유효한 FCM 토큰이 없음 - 재시도 실패: ID={}, 부모ID={}",
                        messageId, message.getParentId());
                handleFailure(message);
                return;
            }

            // 최신 FCM 토큰으로 메시지 전송 시도
            sendFcmMessage(message, fcmToken);

        } catch (Exception e) {
            log.error("메시지 재시도 처리 중 예외 발생: ID={}", messageId, e);

            // 에러 메트릭 증가
            pushMetricsService.incrementProcessingError();
        }
    }

    /**
     * FCM 메시지 전송
     */
    private void sendFcmMessage(PushMessage message, String fcmToken) {
        Timer.Sample sample = pushMetricsService.startPushDeliveryTimer();

        try {
            pushMetricsService.recordFcmRequestTime(() -> {
                try {
                    fcmService.sendMessage(
                            fcmToken,
                            message.getTitle(),
                            message.getContent()
                    );
                    return true;
                } catch (FirebaseMessagingException e) {
                    throw new RuntimeException(e);
                }
            });

            // 성공 처리
            message.setStatus(MessageStatus.COMPLETED);
            pushMessageRepository.save(message);

            // 성공 메트릭 증가
            pushMetricsService.incrementRetrySuccess();

            log.info("FCM 메시지 재시도 성공: ID={}, 시도횟수={}",
                    message.getId(), message.getRetryCount());

        } catch (Exception e) {
            // 실패 메트릭 증가
            pushMetricsService.incrementRetryFailure();

            if (e.getCause() instanceof FirebaseMessagingException fcmEx) {
                if (fcmEx.getMessagingErrorCode() == MessagingErrorCode.INVALID_ARGUMENT) {
                    pushMetricsService.incrementTokenInvalid();
                }

                log.error("FCM 메시지 재시도 실패: ID={}, 시도횟수={}, 오류={}",
                        message.getId(), message.getRetryCount(), fcmEx.getMessage());
            } else {
                log.error("FCM 메시지 재시도 중 예외 발생: ID={}, 시도횟수={}",
                        message.getId(), message.getRetryCount(), e);
            }

            handleFailure(message);
        } finally {
            // 타이머 종료
            pushMetricsService.stopPushDeliveryTimer(sample);
        }
    }

    /**
     * 실패 처리
     */
    private void handleFailure(PushMessage message) {
        // 최대 재시도 횟수 초과 여부 확인
        if (message.getRetryCount() >= MAX_RETRY_ATTEMPTS) {
            // 최종 실패로 처리
            message.setStatus(MessageStatus.FAILED);
            pushMessageRepository.save(message);

            log.warn("최대 재시도 횟수 초과 - 최종 실패 처리: ID={}, 시도횟수={}",
                    message.getId(), message.getRetryCount());
            return;
        }

        // 다음 재시도를 위해 PENDING 상태로 변경
        message.setStatus(MessageStatus.PENDING);

        // 지수 백오프 적용하여 다음 시도 시간 계산
        int delaySeconds = calculateBackoffDelay(message.getRetryCount());
        LocalDateTime nextAttemptTime = LocalDateTime.now().plusSeconds(delaySeconds);
        message.setNextAttemptTime(nextAttemptTime);

        pushMessageRepository.save(message);

        // 다음 재시도 스케줄링
        scheduleNextRetry(message.getId(), delaySeconds);
    }

    /**
     * 지수 백오프 지연 시간 계산
     */
    private int calculateBackoffDelay(int retryCount) {
        int baseDelay = 10;
        int maxDelay = 600; // 10분

        int delay = (int) Math.min(maxDelay, Math.pow(2, retryCount) * baseDelay);
        log.info("다음 재시도 지연 시간: {}초 (재시도횟수: {})", delay, retryCount);
        return delay;
    }

    /**
     * 다음 재시도 스케줄링
     */
    private void scheduleNextRetry(Long messageId, int delaySeconds) {
        try {
            // Redis를 통한 지연 발행
            String message = String.valueOf(messageId);
            redisTemplateManager.scheduleMessage("push-queue:retry", message, delaySeconds);

        } catch (Exception e) {
            log.error("다음 재시도 스케줄링 실패: ID={}", messageId, e);
            pushMetricsService.incrementProcessingError();
        }
    }
}
