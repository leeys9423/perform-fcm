package com.example.fcm.domain.attendance.controller;

import com.example.fcm.domain.attendance.dto.request.AttendanceRequest;
import com.example.fcm.domain.attendance.dto.response.AttendanceResponse;
import com.example.fcm.domain.attendance.service.AttendanceService;
import com.example.fcm.global.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attendances")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping
    public ResponseEntity<BaseResponse<AttendanceResponse>> checkAttendance(@RequestBody @Valid AttendanceRequest request) {
        return ResponseEntity.ok(BaseResponse.of(attendanceService.checkAttendance(request), "출석 체크 완료"));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<AttendanceResponse>>> getAttendances(
            @RequestParam Long studentId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ) {
        return ResponseEntity.ok(BaseResponse.of(attendanceService.getAttendances(studentId, startDate, endDate), "출석 이력 조회 완료"));
    }

    @GetMapping("/today")
    public ResponseEntity<BaseResponse<List<AttendanceResponse>>> getTodayAttendances(
            @RequestParam Long studentId
    ) {
        return ResponseEntity.ok(BaseResponse.of(attendanceService.getTodayAttendances(studentId), "당일 출석 이력 조회 완료"));
    }

    @DeleteMapping("/{attendanceId}")
    public ResponseEntity<BaseResponse<Void>> deleteAttendance(
            @PathVariable Long attendanceId
    ) {
        attendanceService.deleteAttendance(attendanceId);
        return ResponseEntity.ok(BaseResponse.of(null, "출석 삭제 완료"));
    }
}
