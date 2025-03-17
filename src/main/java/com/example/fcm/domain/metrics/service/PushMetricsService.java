package com.example.fcm.domain.metrics.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class PushMetricsService {

    private final MeterRegistry meterRegistry;

    // 카운터
    private Counter pushRequestsTotal;
    private Counter pushSuccessTotal;
    private Counter pushFailureTotal;
    private Counter retryAttemptsTotal;
    private Counter retrySuccessTotal;
    private Counter retryFailureTotal;
    private Counter tokenInvalidTotal;
    private Counter processingErrorTotal;

    // 타이머
    private Timer pushDeliveryTimer;
    private Timer fcmRequestTimer;

    // 게이지
    private AtomicInteger pendingMessagesGauge;
    private AtomicInteger inProgressMessagesGauge;
    private AtomicInteger failedMessagesGauge;

    @PostConstruct
    public void init() {
        // 카운터 초기화
        pushRequestsTotal = Counter.builder("push.requests.total")
                .description("총 푸시 요청 수")
                .register(meterRegistry);

        pushSuccessTotal = Counter.builder("push.success.total")
                .description("성공한 푸시 알림 수")
                .register(meterRegistry);

        pushFailureTotal = Counter.builder("push.failure.total")
                .description("실패한 푸시 알림 수")
                .register(meterRegistry);

        retryAttemptsTotal = Counter.builder("retry.attempts.total")
                .description("총 재시도 횟수")
                .register(meterRegistry);

        retrySuccessTotal = Counter.builder("retry.success.total")
                .description("성공한 재시도 횟수")
                .register(meterRegistry);

        retryFailureTotal = Counter.builder("retry.failure.total")
                .description("실패한 재시도 횟수")
                .register(meterRegistry);

        tokenInvalidTotal = Counter.builder("token.invalid.total")
                .description("유효하지 않은 토큰 수")
                .register(meterRegistry);

        processingErrorTotal = Counter.builder("processing.error.total")
                .description("처리 중 발생한 오류 수")
                .register(meterRegistry);

        // 타이머 초기화
        pushDeliveryTimer = Timer.builder("push.delivery.time")
                .description("푸시 알림 전송 소요 시간")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        fcmRequestTimer = Timer.builder("fcm.request.time")
                .description("FCM 요청 응답 시간")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        // 게이지 초기화
        pendingMessagesGauge = meterRegistry.gauge("messages.pending",
                new AtomicInteger(0));
        inProgressMessagesGauge = meterRegistry.gauge("messages.in_progress",
                new AtomicInteger(0));
        failedMessagesGauge = meterRegistry.gauge("messages.failed",
                new AtomicInteger(0));

        log.info("푸시 알림 메트릭 초기화 완료");
    }

    // 카운터 증가 메서드
    public void incrementPushRequests() {
        pushRequestsTotal.increment();
    }

    public void incrementPushSuccess() {
        pushSuccessTotal.increment();
    }

    public void incrementPushFailure() {
        pushFailureTotal.increment();
    }

    public void incrementRetryAttempts() {
        retryAttemptsTotal.increment();
    }

    public void incrementRetrySuccess() {
        retrySuccessTotal.increment();
    }

    public void incrementRetryFailure() {
        retryFailureTotal.increment();
    }

    public void incrementTokenInvalid() {
        tokenInvalidTotal.increment();
    }

    public void incrementProcessingError() {
        processingErrorTotal.increment();
    }

    // 타이머 측정 메서드
    public Timer.Sample startPushDeliveryTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopPushDeliveryTimer(Timer.Sample sample) {
        sample.stop(pushDeliveryTimer);
    }

    public <T> T recordFcmRequestTime(Supplier<T> operation) {
        return fcmRequestTimer.record(operation);
    }

    public void recordFcmRequestTime(Runnable operation) {
        fcmRequestTimer.record(operation);
    }

    // 게이지 업데이트 메서드
    public void updatePendingMessages(int count) {
        pendingMessagesGauge.set(count);
    }

    public void updateInProgressMessages(int count) {
        inProgressMessagesGauge.set(count);
    }

    public void updateFailedMessages(int count) {
        failedMessagesGauge.set(count);
    }
}
