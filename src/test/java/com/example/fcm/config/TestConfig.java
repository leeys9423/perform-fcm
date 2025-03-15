package com.example.fcm.config;

import com.example.fcm.domain.notification.dto.message.PushMessageEvent;
import com.example.fcm.infra.fcm.FcmService;
import com.example.fcm.infra.redis.PushMessagePublisher;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

@TestConfiguration
@Profile("test") // test 프로필에서만 활성화
public class TestConfig {

    /**
     * FCM 서비스 모킹 - 실제 FCM 메시지를 보내지 않음
     */
    @Bean
    @Primary
    public FcmService mockFcmService() throws FirebaseMessagingException {
        FcmService mockService = mock(FcmService.class);
        // 어떤 매개변수가 들어와도 예외를 발생시키지 않음
        doNothing().when(mockService).sendMessage(any(), any(), any());
        return mockService;
    }

    /**
     * Redis 퍼블리셔 모킹 - 실제 Redis에 메시지를 발행하지 않음
     */
    @Bean
    @Primary
    public PushMessagePublisher mockPushMessagePublisher() {
        PushMessagePublisher mockPublisher = mock(PushMessagePublisher.class);
        // 어떤 이벤트가 들어와도 예외를 발생시키지 않음
        doNothing().when(mockPublisher).publish(any(PushMessageEvent.class));
        return mockPublisher;
    }
}
