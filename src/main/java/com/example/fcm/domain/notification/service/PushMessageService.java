package com.example.fcm.domain.notification.service;

import com.example.fcm.domain.notification.dto.message.PushMessageEvent;
import com.example.fcm.domain.notification.dto.request.PushMessageTestRequest;
import com.example.fcm.domain.notification.entity.MessageStatus;
import com.example.fcm.domain.notification.entity.PushHistory;
import com.example.fcm.domain.notification.entity.PushMessage;
import com.example.fcm.domain.notification.entity.SendResult;
import com.example.fcm.domain.notification.repository.PushHistoryRepository;
import com.example.fcm.domain.notification.repository.PushMessageRepository;
import com.example.fcm.global.error.exception.MessagePublishException;
import com.example.fcm.infra.fcm.FcmService;
import com.example.fcm.infra.redis.PushMessagePublisher;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PushMessageService {

    private final FcmService fcmService;
    private final PushMessagePublisher publisher;
    private final PushMessageRepository pushMessageRepository;
    private final PushHistoryRepository pushHistoryRepository;

    public void sendTestMessage(PushMessageTestRequest request) throws FirebaseMessagingException {
        fcmService.sendMessage(request.getToken(), "제목", "테스트입니다.");
    }

    public PushHistory savePushHistory(PushMessageEvent event, SendResult sendResult) {
        PushHistory history = PushHistory.builder()
                .parentId(event.getParentId())
                .notificationType(event.getType())
                .referenceId(event.getReferenceId())
                .title(event.getTitle())
                .content(event.getContent())
                .sendResult(sendResult)
                .build();

        return pushHistoryRepository.save(history);
    }

    public PushMessage savePushMessage(PushMessageEvent event, Long historyId) {
        PushMessage message = PushMessage.builder()
                .parentId(event.getParentId())
                .historyId(historyId)
                .title(event.getTitle())
                .content(event.getContent())
                .notificationType(event.getType())
                .referenceId(event.getReferenceId())
                .status(MessageStatus.PENDING)
                .retryCount(0)
                .build();

        message.setLastAttemptTime(LocalDateTime.now());
        message.setNextAttemptTime(LocalDateTime.now());

        return pushMessageRepository.save(message);
    }

    // Redis 메시지 큐는 트랜잭션 미지원
    // publish용 메서드
    @Transactional
    public void sendPushMessage(PushMessageEvent event) {
        try {
            publisher.publish(event);
        } catch (MessagePublishException e) {
            log.error("푸시 메시지 발행 실패: {}", e.getMessage());

            // 실패 히스토리 저장
            PushHistory history = savePushHistory(event, SendResult.FAIL);

            // 재시도 메시지 저장
            PushMessage pushMessage = savePushMessage(event, history.getId());

            log.info("재시도 메시지 저장 완료: ID={}", pushMessage.getId());
        }
    }
}
