package com.example.fcm.domain.studentParent.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class StudentParentFcmResponse {

    private Long id;
    private String name;
    private String studentName;
    private String fcmToken;
}
