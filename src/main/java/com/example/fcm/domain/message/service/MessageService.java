package com.example.fcm.domain.message.service;

import com.example.fcm.domain.message.dto.request.MessageTestRequest;
import com.example.fcm.infra.fcm.FcmService;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final FcmService fcmService;

    public void sendTestMessage(MessageTestRequest request) throws FirebaseMessagingException {
        fcmService.sendMessage(request.getToken(), "제목", "테스트입니다.");
    }
}
