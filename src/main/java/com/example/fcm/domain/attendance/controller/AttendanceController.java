package com.example.fcm.domain.attendance.controller;

import com.example.fcm.domain.attendance.dto.request.AttendanceRequest;
import com.example.fcm.domain.attendance.dto.response.AttendanceResponse;
import com.example.fcm.domain.attendance.service.AttendanceService;
import com.example.fcm.global.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attendances")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping
    public ResponseEntity<BaseResponse<AttendanceResponse>> checkAttendance(@RequestBody @Valid AttendanceRequest request) {
        return ResponseEntity.ok(BaseResponse.of(attendanceService.checkAttendance(request), "출석 체크 완료"));
    }
}
