package com.example.fcm.domain.device.exception;

import com.example.fcm.global.error.code.ErrorCode;
import com.example.fcm.global.error.exception.BusinessException;

public class DeviceNotFoundException extends BusinessException {
    public DeviceNotFoundException() {
        super(ErrorCode.DEVICE_NOT_FOUND);
    }
}
