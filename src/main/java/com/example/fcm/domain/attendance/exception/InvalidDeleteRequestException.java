package com.example.fcm.domain.attendance.exception;

import com.example.fcm.global.error.code.ErrorCode;
import com.example.fcm.global.error.exception.BusinessException;

public class InvalidDeleteRequestException extends BusinessException {
    public InvalidDeleteRequestException() {
        super(ErrorCode.INVALID_DELETE_REQUEST);
    }
}
