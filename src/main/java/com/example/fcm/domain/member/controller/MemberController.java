package com.example.fcm.domain.member.controller;

import com.example.fcm.domain.member.dto.request.MemberCreateRequest;
import com.example.fcm.domain.member.dto.request.MemberUpdateRequest;
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
        return ResponseEntity.ok(BaseResponse.of(memberService.createMember(request), "회원 등록 성공"));
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<BaseResponse<Member>> getMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(BaseResponse.of(memberService.getMember(memberId), "회원 조회 성공"));
    }

    @PutMapping("/{memberId}")
    public ResponseEntity<BaseResponse<Member>> updateMember(@RequestBody @Valid MemberUpdateRequest request, @PathVariable Long memberId) {
        return ResponseEntity.ok(BaseResponse.of(memberService.updateMember(request, memberId), "회원 수정 완료"));
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<BaseResponse<Member>> deleteMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(BaseResponse.of(memberService.deleteMember(memberId), "회원 삭제 완료"));
    }
}
