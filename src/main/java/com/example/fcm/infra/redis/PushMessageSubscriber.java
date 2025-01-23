package com.example.fcm.infra.redis;

import com.example.fcm.domain.notification.dto.message.PushMessageEvent;
import com.example.fcm.domain.notification.entity.PushHistory;
import com.example.fcm.domain.notification.entity.SendResult;
import com.example.fcm.domain.notification.service.PushMessageService;
import com.example.fcm.infra.fcm.FcmService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PushMessageSubscriber {

    private final FcmService fcmService;
    private final PushMessageService pushMessageService;

    public void handleMessage(String message) {
        PushMessageEvent event = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            event = objectMapper.readValue(message, PushMessageEvent.class);

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

        } catch (Exception e) {
            log.error("Failed to process push message: {}", message, e);
        }
    }
}
