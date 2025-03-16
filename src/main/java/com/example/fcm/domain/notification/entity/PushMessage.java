package com.example.fcm.domain.notification.entity;

import com.example.fcm.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
    @Setter
    private MessageStatus status;

    @Setter
    private int retryCount;

    // 마지막 시도 시간 추가
    @Setter
    private LocalDateTime lastAttemptTime;

    // 다음 시도 예정 시간 추가
    @Setter
    private LocalDateTime nextAttemptTime;

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
