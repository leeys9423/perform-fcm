package com.example.fcm.domain.pushMessage.service;

import com.example.fcm.domain.pushMessage.dto.request.PushMessageTestRequest;
import com.example.fcm.infra.fcm.FcmService;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PushMessageService {

    private final FcmService fcmService;

    public void sendTestMessage(PushMessageTestRequest request) throws FirebaseMessagingException {
        fcmService.sendMessage(request.getToken(), "제목", "테스트입니다.");
    }
}
