package com.example.fcm.domain.attendance.service;

import com.example.fcm.domain.attendance.dto.request.AttendanceRequest;
import com.example.fcm.domain.attendance.dto.response.AttendanceResponse;
import com.example.fcm.domain.attendance.entity.Attendance;
import com.example.fcm.domain.attendance.entity.AttendanceType;
import com.example.fcm.domain.attendance.exception.AttendanceNotFoundException;
import com.example.fcm.domain.attendance.exception.InvalidDeleteRequestException;
import com.example.fcm.domain.attendance.repository.AttendanceRepository;
import com.example.fcm.domain.attendance.repository.AttendanceRepositoryCustom;
import com.example.fcm.domain.member.entity.Member;
import com.example.fcm.domain.member.facade.MemberFacade;
import com.example.fcm.domain.notification.dto.message.PushMessageEvent;
import com.example.fcm.domain.notification.service.PushMessageService;
import com.example.fcm.domain.studentParent.dto.response.StudentParentFcmResponse;
import com.example.fcm.domain.studentParent.facade.StudentParentFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final AttendanceRepositoryCustom attendanceRepositoryCustom;
    private final MemberFacade memberFacade;
    private final StudentParentFacade studentParentFacade;
    private final PushMessageService pushMessageService;

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
                .attendanceDate(LocalDate.now())
                .build();
        attendanceRepository.save(attendance);

        List<StudentParentFcmResponse> tokens = studentParentFacade.getParentsFcmTokensByStudentId(request.getStudentId());
        for (StudentParentFcmResponse token : tokens) {
            PushMessageEvent event = PushMessageEvent.ofAttendance(
                    member.getName(),
                    attendance.getCheckTime(),
                    token.getId(),
                    attendance.getId(),
                    token.getFcmToken(),
                    attendance.getType());

            pushMessageService.sendPushMessage(event);
        }

        return AttendanceResponse.of(attendance, member.getName());
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponse> getAttendances(Long studentId, LocalDate startDate, LocalDate endDate) {
        // 학생 존재 여부 확인
        Member member = memberFacade.getMember(studentId);

        return attendanceRepositoryCustom.findAttendances(studentId, startDate, endDate).stream()
                .map(attendance -> AttendanceResponse.of(attendance, member.getName()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponse> getTodayAttendances(Long studentId) {
        // 학생 존재 여부 확인
        Member member = memberFacade.getMember(studentId);

        return attendanceRepository.findTodayAttendances(studentId).stream()
                .map(attendance -> AttendanceResponse.of(attendance, member.getName()))
                .toList();
    }

    @Transactional
    public void deleteAttendance(Long attendanceId) {
        Attendance attendance = attendanceRepository.findById(attendanceId).orElseThrow(AttendanceNotFoundException::new);

        // 당일 데이터만 삭제 가능하도록
        if (!attendance.getAttendanceDate().equals(LocalDate.now())) {
            throw new InvalidDeleteRequestException();
        }

        attendanceRepository.delete(attendance);
    }
}
