package com.example.fcm.domain.notification.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
    ATTENDANCE("출석 알림"),
    HOMEWORK("과제 알림"),
    NOTICE("공지사항"),
    EVENT("행사 알림");
    private final String text;
}
