package com.example.fcm.domain.pushMessage.controller;

import com.example.fcm.domain.pushMessage.dto.request.PushMessageTestRequest;
import com.example.fcm.domain.pushMessage.service.PushMessageService;
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
public class PushMessageController {

    private final PushMessageService pushMessageService;

    @PostMapping("/test")
    public ResponseEntity<BaseResponse<Void>> testMessage(
            @RequestBody @Valid PushMessageTestRequest request
    ) throws FirebaseMessagingException {
        pushMessageService.sendTestMessage(request);
        return ResponseEntity.ok(BaseResponse.of(null, "푸시 알림 전송 성공"));
    }
}
