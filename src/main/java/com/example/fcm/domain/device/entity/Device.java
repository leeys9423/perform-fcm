package com.example.fcm.domain.device.entity;

import com.example.fcm.global.common.BaseEntity;
import com.example.fcm.global.common.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "devices")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Device extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long parentId;
    private String fcmToken;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public Device(Long parentId, String fcmToken, Status status) {
        this.parentId = parentId;
        this.fcmToken = fcmToken;
        this.status = status;
    }
}
