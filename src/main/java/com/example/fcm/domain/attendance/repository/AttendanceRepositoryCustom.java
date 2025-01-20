package com.example.fcm.domain.attendance.repository;

import com.example.fcm.domain.attendance.entity.Attendance;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepositoryCustom {

    List<Attendance> findAttendances(Long studentId, LocalDate startDate, LocalDate endDate);
}
