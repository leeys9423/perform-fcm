package com.example.fcm.domain.attendance.repository;

import com.example.fcm.domain.attendance.entity.Attendance;
import com.example.fcm.domain.attendance.entity.QAttendance;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AttendanceRepositoryCustomImpl implements AttendanceRepositoryCustom {
    private static final QAttendance attendance = QAttendance.attendance;
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Attendance> findAttendances(Long studentId, LocalDate startDate, LocalDate endDate) {
        return queryFactory
                .selectFrom(attendance)
                .where(attendance.studentId.eq(studentId),
                        dateRange(startDate, endDate))
                .fetch();
    }

    private BooleanBuilder dateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null && endDate == null) {
            return null;
        }

        BooleanBuilder builder = new BooleanBuilder();

        if (startDate != null) {
            builder.and(attendance.attendanceDate.goe(startDate));
        }

        if (endDate != null) {
            builder.and(attendance.attendanceDate.loe(endDate));
        }

        return builder;
    }
}
