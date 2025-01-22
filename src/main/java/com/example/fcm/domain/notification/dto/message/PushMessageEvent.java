package com.example.fcm.domain.notification.dto.message;

import com.example.fcm.domain.attendance.entity.AttendanceType;
import com.example.fcm.domain.notification.entity.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PushMessageEvent {

    private String title;
    private String content;
    private Long parentId;
    private NotificationType type;
    private Long referenceId;
    private String fcmToken;
    private LocalDateTime sendTime;

    public static PushMessageEvent ofAttendance(
            String name,
            LocalDateTime checkTime,
            Long parentId,
            Long attendanceId,
            String fcmToken,
            AttendanceType type) {

        String title = type == AttendanceType.CHECKIN ? "등원 알림" : "하원 알림";
        String timeStr = checkTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        String content = String.format("%s님이 %s에 %s하였습니다.",
                name,
                timeStr,
                type == AttendanceType.CHECKIN ? "등원" : "하원");

        return PushMessageEvent.builder()
                .title(title)
                .content(content)
                .parentId(parentId)
                .type(NotificationType.ATTENDANCE)
                .referenceId(attendanceId)
                .fcmToken(fcmToken)
                .sendTime(LocalDateTime.now())
                .build();
    }
}
