package com.example.fcm.global.error.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 공통 에러 코드
    INVALID_INPUT_VALUE("COM-001", HttpStatus.BAD_REQUEST, "잘못된 입력값입니다."),
    INTERNAL_SERVER_ERROR("COM-002", HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),

    // 회원 관련 에러 코드
    MEMBER_NOT_FOUND("MEM-001", HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),

    // 부모 관련 에러 코드
    PARENT_NOT_FOUND("PAR-001", HttpStatus.NOT_FOUND, "부모 회원을 찾을 수 없습니다."),

    // 기기 관련 에러 코드
    DEVICE_NOT_FOUND("DEV-001", HttpStatus.NOT_FOUND, "기기를 찾을 수 없습니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
