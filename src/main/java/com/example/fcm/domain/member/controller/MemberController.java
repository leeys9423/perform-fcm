package com.example.fcm.domain.member.controller;

import com.example.fcm.domain.member.dto.request.MemberCreateRequest;
import com.example.fcm.domain.member.entity.Member;
import com.example.fcm.domain.member.service.MemberService;
import com.example.fcm.global.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<BaseResponse<Long>> createMember(@RequestBody @Valid MemberCreateRequest request) {
        Long id = memberService.createMember(request);
        return ResponseEntity.ok(BaseResponse.of(id, "회원 등록 성공"));
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<BaseResponse<Member>> getMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(BaseResponse.of(memberService.getMember(memberId), "회원 조회 성공"));
    }
}
