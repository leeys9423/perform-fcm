package com.example.fcm.global.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseResponse<T> {

    private T data;
    private String message;
    private LocalDateTime timestamp;

    public BaseResponse(T data, String message) {
        this.data = data;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> BaseResponse<T> of(T data, String message) {
        return new BaseResponse<>(data, message);
    }
}
