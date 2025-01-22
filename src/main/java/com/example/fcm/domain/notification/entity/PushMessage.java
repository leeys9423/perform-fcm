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
public class PushMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long parentId;
    private Long historyId;

    private String title;
    private String content;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    private Long referenceId; // 각 알림별 타입 참조 ID

    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    private int retryCount;

    @Builder
    public PushMessage(Long parentId, Long historyId, String title, String content, NotificationType notificationType, Long referenceId, MessageStatus status, int retryCount) {
        this.parentId = parentId;
        this.historyId = historyId;
        this.title = title;
        this.content = content;
        this.notificationType = notificationType;
        this.referenceId = referenceId;
        this.status = status;
        this.retryCount = retryCount;
    }
}
