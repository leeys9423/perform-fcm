package com.example.fcm.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {

    ACTIVE("활성화"), INACTIVE("비활성화");

    private final String text;
}
