package com.example.fcm.domain.member.facade;

import com.example.fcm.domain.member.entity.Member;
import com.example.fcm.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberFacade {

    private final MemberService memberService;

    public Member getMember(Long memberId) {
        return memberService.getMember(memberId);
    }
}
