package com.example.fcm.infra.redis;

import com.example.fcm.domain.notification.dto.message.PushMessageEvent;
import com.example.fcm.domain.notification.entity.PushHistory;
import com.example.fcm.domain.notification.entity.SendResult;
import com.example.fcm.domain.notification.service.PushMessageService;
import com.example.fcm.infra.fcm.FcmService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PushMessageSubscriber {

    private final FcmService fcmService;
    private final PushMessageService pushMessageService;
    private final ObjectMapper objectMapper;
    private final AsyncTaskExecutor asyncTaskExecutor;

    public void handleMessage(String message) {
        try {
            PushMessageEvent event = objectMapper.readValue(message, PushMessageEvent.class);

            // FCM 전송과 이력 저장을 비동기로 처리
            asyncTaskExecutor.execute(() -> processMessageAsync(event));

        } catch (Exception e) {
            log.error("Failed to parse push message: {}", message, e);
        }
    }

    private void processMessageAsync(PushMessageEvent event) {
        try {
            // FCM 전송
            fcmService.sendMessage(
                    event.getFcmToken(),
                    event.getTitle(),
                    event.getContent()
            );

            // 성공 이력 저장
            pushMessageService.savePushHistory(event, SendResult.SUCCESS);

        } catch (Exception e) {
            // FCM 전송 실패 시 실패 이력 저장
            log.error("Failed to send FCM message: {}", event, e);
            PushHistory history = pushMessageService.savePushHistory(event, SendResult.FAIL);
            pushMessageService.savePushMessage(event, history.getId());
        }
    }
}
