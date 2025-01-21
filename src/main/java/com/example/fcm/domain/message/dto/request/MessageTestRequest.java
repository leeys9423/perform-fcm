package com.example.fcm.domain.message.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageTestRequest {

    @NotBlank(message = "토큰을 입력해주세요.")
    private String token;
}
