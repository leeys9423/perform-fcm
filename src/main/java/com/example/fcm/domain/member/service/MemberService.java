package com.example.fcm.domain.member.service;

import com.example.fcm.domain.member.dto.request.MemberCreateRequest;
import com.example.fcm.domain.member.dto.request.MemberUpdateRequest;
import com.example.fcm.domain.member.dto.response.MemberResponse;
import com.example.fcm.domain.member.entity.Member;
import com.example.fcm.domain.member.exception.MemberNotFoundException;
import com.example.fcm.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long createMember(MemberCreateRequest request) {
        Member member = request.toEntity();
        return memberRepository.save(member).getId();
    }

    public MemberResponse getMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        return MemberResponse.of(member);
    }

    @Transactional
    public MemberResponse updateMember(MemberUpdateRequest request, Long memberId) {
        Member member = memberRepository.findById(memberId)
                                        .orElseThrow(MemberNotFoundException::new);

        member.changeName(request.getName());
        memberRepository.save(member);

        return MemberResponse.of(member);
    }

    @Transactional
    public MemberResponse deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        member.inactive();
        memberRepository.save(member);

        return MemberResponse.of(member);
    }
}
