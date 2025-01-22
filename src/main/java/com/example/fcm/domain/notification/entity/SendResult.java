package com.example.fcm.domain.notification.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SendResult {
    SUCCESS("성공"), FAIL("실패");
    private final String text;
}
