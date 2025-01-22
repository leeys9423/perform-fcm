package com.example.fcm.domain.notification.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MessageStatus {
    PENDING("재시도 대기"),       // 최초 실패 시 저장되는 상태
    IN_PROGRESS("재시도 처리중"), // 재시도 작업이 메시지를 처리 중인 상태
    COMPLETED("완료"),          // 재시도 성공으로 더 이상 재시도가 필요 없는 상태
    FAILED("최종 실패");         // 최대 재시도 횟수(5회) 초과로 인한 최종 실패 상태

    private final String text;
}
