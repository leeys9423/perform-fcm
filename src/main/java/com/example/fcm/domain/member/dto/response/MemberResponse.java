package com.example.fcm.domain.member.dto.response;

import com.example.fcm.domain.member.entity.Member;
import com.example.fcm.domain.member.entity.MemberStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MemberResponse {

    private String name;
    private MemberStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static MemberResponse of(Member member) {
        return MemberResponse.builder()
                .name(member.getName())
                .status(member.getStatus())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }
}
