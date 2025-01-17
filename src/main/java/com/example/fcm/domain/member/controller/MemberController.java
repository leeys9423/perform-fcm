package com.example.fcm.domain.member.controller;

import com.example.fcm.domain.member.dto.request.MemberCreateRequest;
import com.example.fcm.domain.member.service.MemberService;
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
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<BaseResponse<Long>> createMember(@RequestBody @Valid MemberCreateRequest request) {
        Long id = memberService.save(request);
        return ResponseEntity.ok(BaseResponse.of(id, "회원 등록 성공"));
    }

}
