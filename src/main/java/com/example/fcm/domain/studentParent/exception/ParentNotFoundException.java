package com.example.fcm.domain.studentParent.exception;

import com.example.fcm.global.error.code.ErrorCode;
import com.example.fcm.global.error.exception.BusinessException;

public class ParentNotFoundException extends BusinessException {
    public ParentNotFoundException() {
        super(ErrorCode.PARENT_NOT_FOUND);
    }
}
