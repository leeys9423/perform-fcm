package com.example.fcm.domain.member.service;

import com.example.fcm.domain.member.dto.request.MemberCreateRequest;
import com.example.fcm.domain.member.entity.Member;
import com.example.fcm.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long save(MemberCreateRequest request) {
        Member member = request.toEntity();
        return memberRepository.save(member).getId();
    }
}
