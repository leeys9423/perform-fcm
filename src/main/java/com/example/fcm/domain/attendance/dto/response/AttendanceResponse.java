package com.example.fcm.domain.attendance.dto.response;

import com.example.fcm.domain.attendance.entity.Attendance;
import com.example.fcm.domain.attendance.entity.AttendanceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class AttendanceResponse {

    private Long id;
    private AttendanceType type;
    private LocalDateTime checkTime;
    private String studentName;

    public static AttendanceResponse of(Attendance attendance, String studentName) {
        return AttendanceResponse.builder()
                .id(attendance.getId())
                .type(attendance.getType())
                .checkTime(attendance.getCheckTime())
                .studentName(studentName)
                .build();
    }
}
