package com.example.fcm.domain.attendance.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AttendanceType {

    CHECKIN("등원"), CHECKOUT("하원");

    private final String text;
}
