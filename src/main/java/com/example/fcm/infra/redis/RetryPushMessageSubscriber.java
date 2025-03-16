package com.example.fcm.infra.redis;

import com.example.fcm.domain.notification.service.RetryMessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RetryPushMessageSubscriber {

    private final RetryMessageService retryMessageService;
    private final ObjectMapper objectMapper;

    // Redis의 retry 채널에서 메시지를 수신해 처리하는 메서드
    public void handleRetryMessage(String message) {
        try {
            // 메시지에서 ID 추출 (단순 문자열을 숫자로 변환)
            Long messageId = Long.parseLong(message);
            log.info("재시도 메시지 수신: ID={}", messageId);

            // 서비스에 처리 위임
            retryMessageService.processRetryById(messageId);

        } catch (Exception e) {
            log.error("재시도 메시지 처리 중 예외 발생", e);
        }
    }
}
