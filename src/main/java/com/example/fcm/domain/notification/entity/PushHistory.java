package com.example.fcm.domain.notification.entity;

import com.example.fcm.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PushHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long parentId;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    private Long referenceId;
    private String title;
    private String content;

    @Enumerated(EnumType.STRING)
    private SendResult sendResult;

    @Builder
    public PushHistory(Long parentId, NotificationType notificationType, Long referenceId, String title, String content, SendResult sendResult) {
        this.parentId = parentId;
        this.notificationType = notificationType;
        this.referenceId = referenceId;
        this.title = title;
        this.content = content;
        this.sendResult = sendResult;
    }
}
