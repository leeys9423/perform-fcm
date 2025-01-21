package com.example.fcm.domain.message.controller;

import com.example.fcm.domain.message.dto.request.MessageTestRequest;
import com.example.fcm.domain.message.service.MessageService;
import com.example.fcm.global.common.BaseResponse;
import com.google.firebase.messaging.FirebaseMessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/test")
    public ResponseEntity<BaseResponse<Void>> testMessage(
            @RequestBody @Valid MessageTestRequest request
    ) throws FirebaseMessagingException {
        messageService.sendTestMessage(request);
        return ResponseEntity.ok(BaseResponse.of(null, "푸시 알림 전송 성공"));
    }
}
