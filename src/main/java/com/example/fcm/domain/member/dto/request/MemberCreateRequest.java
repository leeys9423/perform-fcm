package com.example.fcm.domain.member.dto.request;

import com.example.fcm.domain.member.entity.Member;
import com.example.fcm.global.common.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberCreateRequest {

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;


    public Member toEntity() {
        return Member.builder()
                        .name(name)
                        .status(Status.ACTIVE)
                        .build();
    }
}
