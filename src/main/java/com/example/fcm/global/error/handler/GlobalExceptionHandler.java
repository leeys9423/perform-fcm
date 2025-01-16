package com.example.fcm.global.error.handler;

import com.example.fcm.global.error.code.ErrorCode;
import com.example.fcm.global.error.exception.BusinessException;
import com.example.fcm.global.error.exception.FcmException;
import com.example.fcm.global.error.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // validation 관련
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        log.error("Validation error occurred: {}", e.getMessage(), e);
        String message = e.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();

        return createErrorResponse(ErrorCode.INVALID_INPUT_VALUE, message);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.error("Business error occurred: {}", e.getMessage(), e);
        return createErrorResponse(e.getErrorCode());
    }

    @ExceptionHandler(FcmException.class)
    public ResponseEntity<ErrorResponse> handleFcmException(FcmException e) {
        log.error("FCM error occurred: {}", e.getMessage(), e);
        return createErrorResponse(e.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Unexpected error occurred: {}", e.getMessage(), e);
        return createErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new ErrorResponse(errorCode.getCode(), errorCode.getMessage()));
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(ErrorCode errorCode, String message) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new ErrorResponse(errorCode.getCode(), message));
    }
}
