package com.example.fcm.infra.redis;

import com.example.fcm.domain.notification.dto.message.PushMessageEvent;
import com.example.fcm.global.error.code.ErrorCode;
import com.example.fcm.global.error.exception.MessagePublishException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PushMessagePublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(PushMessageEvent notification) {    // 파라미터 타입도 변경
        try {
            redisTemplate.convertAndSend("push-queue:normal", notification);
        } catch (Exception e) {
            log.error("푸시 알림 발행 실패: {}", notification, e);
            throw new MessagePublishException(ErrorCode.MESSAGE_PUBLISH_FAILED);
        }
    }
}
