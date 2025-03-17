package com.example.fcm.domain.metrics.scheduler;

import com.example.fcm.domain.metrics.service.PushMetricsService;
import com.example.fcm.domain.notification.entity.MessageStatus;
import com.example.fcm.domain.notification.repository.PushMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MetricsScheduler {

    private final PushMetricsService pushMetricsService;
    private final PushMessageRepository pushMessageRepository;

    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void updateMessageStatusMetrics() {
        try {
            int pendingCount = pushMessageRepository.countByStatus(MessageStatus.PENDING);
            int inProgressCount = pushMessageRepository.countByStatus(MessageStatus.IN_PROGRESS);
            int failedCount = pushMessageRepository.countByStatus(MessageStatus.FAILED);

            pushMetricsService.updatePendingMessages(pendingCount);
            pushMetricsService.updateInProgressMessages(inProgressCount);
            pushMetricsService.updateFailedMessages(failedCount);

            log.debug("메트릭 업데이트 완료: pending={}, inProgress={}, failed={}",
                    pendingCount, inProgressCount, failedCount);
        } catch (Exception e) {
            log.error("메트릭 업데이트 중 오류 발생", e);
        }
    }

}
