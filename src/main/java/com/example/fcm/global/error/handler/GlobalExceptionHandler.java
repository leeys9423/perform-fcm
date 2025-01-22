package com.example.fcm.global.error.handler;

import com.example.fcm.global.error.code.ErrorCode;
import com.example.fcm.global.error.exception.BusinessException;
import com.example.fcm.global.error.exception.FcmException;
import com.example.fcm.global.error.exception.MessagePublishException;
import com.example.fcm.global.error.response.ErrorResponse;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MessagingErrorCode;
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

    @ExceptionHandler(FirebaseMessagingException.class)
    public ResponseEntity<ErrorResponse> handleFirebaseException(FirebaseMessagingException e) {
        if (e.getMessagingErrorCode() == MessagingErrorCode.INVALID_ARGUMENT) {
            log.error("Invalid FCM token error: {}", e.getMessage(), e);
            return createErrorResponse(ErrorCode.FCM_TOKEN_INVALID);
        }

        if (e.getMessagingErrorCode() == MessagingErrorCode.UNAVAILABLE ||
                e.getMessagingErrorCode() == MessagingErrorCode.INTERNAL) {
            log.error("FCM server error: {}", e.getMessage(), e);
            return createErrorResponse(ErrorCode.FCM_SERVER_ERROR);
        }

        // 그 외
        log.error("Firebase messaging error: {}", e.getMessage(), e);
        return createErrorResponse(ErrorCode.FCM_SEND_FAILED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Unexpected error occurred: {}", e.getMessage(), e);
        return createErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MessagePublishException.class)
    public ResponseEntity<ErrorResponse> handleMessagePublishException(MessagePublishException e) {
        log.error("Message publish error occurred: {}", e.getMessage(), e);
        return createErrorResponse(e.getErrorCode());
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
