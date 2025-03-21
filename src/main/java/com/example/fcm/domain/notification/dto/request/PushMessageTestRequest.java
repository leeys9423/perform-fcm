package com.example.fcm.domain.notification.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PushMessageTestRequest {

    @NotBlank(message = "토큰을 입력해주세요.")
    private String token;
}
