package com.example.fcm.domain.device.dto.response;

import com.example.fcm.global.common.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DeviceResponse {

    private Long id;
    private String name;
    private String fcmToken;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
