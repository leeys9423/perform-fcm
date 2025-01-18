package com.example.fcm.domain.studentParent.controller;

import com.example.fcm.domain.studentParent.dto.request.StudentParentCreateRequest;
import com.example.fcm.domain.studentParent.dto.response.StudentParentResponse;
import com.example.fcm.domain.studentParent.service.StudentParentService;
import com.example.fcm.global.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student-parents")
public class StudentParentController {

    private final StudentParentService studentParentService;

    @PostMapping
    public ResponseEntity<BaseResponse<Long>> createStudentParent(@RequestBody @Valid StudentParentCreateRequest request) {
        return ResponseEntity.ok(BaseResponse.of(studentParentService.createStudentParent(request), "부모 등록 성공"));
    }

    @GetMapping("/{parentId}")
    public ResponseEntity<BaseResponse<StudentParentResponse>> getStudentParent(@PathVariable Long parentId) {
        return ResponseEntity.ok(BaseResponse.of(studentParentService.getStudentParent(parentId), "부모 조회 성공"));
    }
}
