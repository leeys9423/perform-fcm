package com.example.fcm.domain.attendance.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Table(name = "attendances")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;

    @Enumerated(EnumType.STRING)
    private AttendanceType type;
    private LocalDateTime checkTime;

    // 조회 성능을 위한 컬럼을 따로 만듦
    private LocalDate attendanceDate;

    @Builder
    public Attendance(Long studentId, AttendanceType type, LocalDateTime checkTime, LocalDate attendanceDate) {
        this.studentId = studentId;
        this.type = type;
        this.checkTime = checkTime;
        this.attendanceDate = attendanceDate;
    }
}
