package com.example.fcm.domain.attendance.service;

import com.example.fcm.domain.attendance.dto.request.AttendanceRequest;
import com.example.fcm.domain.attendance.dto.response.AttendanceResponse;
import com.example.fcm.domain.attendance.entity.Attendance;
import com.example.fcm.domain.attendance.entity.AttendanceType;
import com.example.fcm.domain.attendance.repository.AttendanceRepository;
import com.example.fcm.domain.member.entity.Member;
import com.example.fcm.domain.member.facade.MemberFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final MemberFacade memberFacade;

    @Transactional
    public AttendanceResponse checkAttendance(AttendanceRequest request) {
        // 학생 존재 여부 확인
        Member member = memberFacade.getMember(request.getStudentId());

        // 오늘 마지막 출석 기록 확인
        LocalDate today = LocalDate.now();
        Optional<Attendance> lastAttendance = attendanceRepository
                .findTopByStudentIdAndCheckTimeBetweenOrderByCheckTimeDesc(
                        request.getStudentId(),
                        today.atStartOfDay(),
                        today.atTime(LocalTime.MAX)
                );

        // 출석 타입 결정
        AttendanceType type = lastAttendance.filter(attendance -> attendance.getType() == AttendanceType.CHECKIN)
                .map(attendance -> AttendanceType.CHECKOUT)
                .orElse(AttendanceType.CHECKIN);

        // 출석 기록 저장
        Attendance attendance = Attendance.builder()
                .studentId(request.getStudentId())
                .type(type)
                .checkTime(LocalDateTime.now())
                .build();
        attendanceRepository.save(attendance);

        return AttendanceResponse.of(attendance, member.getName());
    }
}
