package com.example.fcm.infra.fcm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisTemplateManager {

    private final RedisTemplate<String, Object> redisTemplate;

    public void publishMessage(String channel, Object message) {
        try {
            redisTemplate.convertAndSend(channel, message);
            log.debug("메시지 발행 성공: 채널={}", channel);
        } catch (Exception e) {
            log.error("메시지 발행 실패: 채널={}", channel, e);
            throw new RuntimeException("메시지 발행 실패", e);
        }
    }

    public void scheduleMessage(String channel, Object message, int delaySeconds) {
        // 고유한 키 생성
        String key = "scheduled:message:" + System.currentTimeMillis() + ":" + Math.random();

        try {
            // Redis에 메시지 정보 저장
            String value = channel + ":" + message;

            // 키 만료 설정과 함께 저장
            redisTemplate.opsForValue().set(key, value);
            redisTemplate.expire(key, delaySeconds, TimeUnit.SECONDS);

            log.debug("지연 메시지 스케줄링 성공: 채널={}, 지연={}초", channel, delaySeconds);
        } catch (Exception e) {
            log.error("지연 메시지 스케줄링 실패: 채널={}, 지연={}초", channel, delaySeconds, e);
            throw new RuntimeException("지연 메시지 스케줄링 실패", e);
        }
    }

    @Async
    public void scheduleMessageWithSleep(String channel, Object message, int delaySeconds) {
        try {
            // 지정된 시간만큼 대기
            Thread.sleep(delaySeconds * 1000L);

            // 지연 후 메시지 발행
            publishMessage(channel, message);

            log.debug("지연 메시지 발행 성공: 채널={}, 지연={}초", channel, delaySeconds);
        } catch (InterruptedException e) {
            log.error("지연 메시지 대기 중 인터럽트 발생: 채널={}, 지연={}초", channel, delaySeconds);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("지연 메시지 발행 실패: 채널={}, 지연={}초", channel, delaySeconds, e);
        }
    }
}
