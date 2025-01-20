package com.example.fcm.domain.attendance.repository;

import com.example.fcm.domain.attendance.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    @Query("select a from Attendance a " +
            "where a.studentId = :studentId " +
            "and a.checkTime between :startTime and :endTime " +
            "order by a.checkTime desc")
    Optional<Attendance> findTopByStudentIdAndCheckTimeBetweenOrderByCheckTimeDesc(Long studentId, LocalDateTime startTime, LocalDateTime endTime);
}
