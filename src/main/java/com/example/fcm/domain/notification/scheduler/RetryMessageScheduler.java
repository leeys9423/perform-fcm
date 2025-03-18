package com.example.fcm.domain.notification.scheduler;

import com.example.fcm.domain.notification.entity.MessageStatus;
import com.example.fcm.domain.notification.entity.PushMessage;
import com.example.fcm.domain.notification.repository.PushMessageRepository;
import com.example.fcm.infra.redis.RedisTemplateManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RetryMessageScheduler {

    private final PushMessageRepository pushMessageRepository;
    private final RedisTemplateManager redisTemplateManager;
    private final ObjectMapper objectMapper;

    private static final String RETRY_CHANNEL = "push-queue:retry";

    /**
     * 30초마다 실행 - PENDING 상태의 메시지 중 nextAttemptTime이 현재보다 이전인 메시지 재시도
     */
    @Scheduled(fixedRate = 30000)
    @Transactional(readOnly = true)
    public void schedulePendingMessages() {
        try {
            // 현재 시간 이전에 재시도 예정인 PENDING 메시지 조회
            LocalDateTime now = LocalDateTime.now();
            List<PushMessage> pendingMessages = pushMessageRepository.findByStatusAndNextAttemptTimeBefore(
                    MessageStatus.PENDING, now);

            if (pendingMessages.isEmpty()) {
                return;
            }

            log.info("재시도 대상 메시지 {}개 발견, 재시도 큐로 발행", pendingMessages.size());

            // 각 메시지를 재시도 큐로 발행
            int successCount = 0;
            for (PushMessage message : pendingMessages) {
                try {
                    // 메시지 ID만 전송
                    String messageJson = String.valueOf(message.getId());
                    redisTemplateManager.publishMessage(RETRY_CHANNEL, messageJson);
                    successCount++;
                } catch (Exception e) {
                    log.error("메시지 재시도 큐 발행 실패: ID={}", message.getId(), e);
                }
            }

            log.info("재시도 큐 발행 완료: 성공={}, 실패={}",
                    successCount, pendingMessages.size() - successCount);

        } catch (Exception e) {
            log.error("재시도 메시지 스케줄링 중 오류 발생", e);
        }
    }

    /**
     * 매일 자정에 실행 - 장기간 처리되지 않은 메시지 정리
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void cleanupStalledMessages() {
        try {
            // 1일 이상 경과된 PENDING, IN_PROGRESS 메시지를 FAILED로 처리
            LocalDateTime cutoffTime = LocalDateTime.now().minusDays(1);
            List<MessageStatus> statuses = Arrays.asList(MessageStatus.PENDING, MessageStatus.IN_PROGRESS);
            int updatedCount = pushMessageRepository.updateStatusForStalledMessages(MessageStatus.FAILED, statuses, cutoffTime);

            if (updatedCount > 0) {
                log.info("장기간 처리되지 않은 메시지 {}개를 FAILED로 처리", updatedCount);
            }
        } catch (Exception e) {
            log.error("장기간 처리되지 않은 메시지 처리 중 오류 발생", e);
        }
    }
}
