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
    DEVICE_NOT_FOUND("DEV-001", HttpStatus.NOT_FOUND, "기기를 찾을 수 없습니다."),

    // 출석 관련 에러 코드
    ATTENDANCE_NOT_FOUND("ATT-001", HttpStatus.NOT_FOUND, "출석 정보를 찾을 수 없습니다."),
    INVALID_DELETE_REQUEST("ATT-002", HttpStatus.BAD_REQUEST, "당일 데이터만 삭제 가능합니다."),

    // Firebase 관련 에러 코드
    FCM_TOKEN_INVALID("FCM-001", HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다."),
    FCM_SEND_FAILED("FCM-002", HttpStatus.INTERNAL_SERVER_ERROR, "메시지 전송에 실패했습니다."),
    FCM_SERVER_ERROR("FCM-003", HttpStatus.INTERNAL_SERVER_ERROR, "FCM 서버 오류가 발생했습니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
