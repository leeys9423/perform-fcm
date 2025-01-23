package com.example.fcm.global.error.exception;

import com.example.fcm.global.error.code.ErrorCode;
import lombok.Getter;

@Getter
public class MessagePublishException extends RuntimeException {

    private final ErrorCode errorCode;

    public MessagePublishException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
