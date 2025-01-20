package com.example.fcm.domain.attendance.exception;

import com.example.fcm.global.error.code.ErrorCode;
import com.example.fcm.global.error.exception.BusinessException;

public class AttendanceNotFoundException extends BusinessException {
    public AttendanceNotFoundException() {
        super(ErrorCode.ATTENDANCE_NOT_FOUND);
    }
}
