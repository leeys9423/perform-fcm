package com.example.fcm.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfig {

    @Bean
    public AsyncTaskExecutor asyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // CPU 코어 수 확인
        int corePoolSize = Runtime.getRuntime().availableProcessors();
        // 일반적인 공식
        // CPU 코어의 수 + 1
        // 주 작업이 I/O 일 경우 : CPU * 2
        executor.setCorePoolSize(corePoolSize * 2);
        // 코어 사이즈 * 2
        executor.setMaxPoolSize(corePoolSize * 4);
        // 초당 예상 요청 수 * 평균 처리 시간(초)
        // 최대 동시 요청 건수 300으로 예상
        executor.setQueueCapacity(300);
        executor.setThreadNamePrefix("async-push-");
        executor.initialize();
        return executor;
    }

}
