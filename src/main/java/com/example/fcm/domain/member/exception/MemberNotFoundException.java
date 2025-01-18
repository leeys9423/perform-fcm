package com.example.fcm.domain.member.exception;

import com.example.fcm.global.error.code.ErrorCode;
import com.example.fcm.global.error.exception.BusinessException;

public class MemberNotFoundException extends BusinessException {
    public MemberNotFoundException() {
        super(ErrorCode.MEMBER_NOT_FOUND);
    }
}
