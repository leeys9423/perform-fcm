package com.example.fcm.domain.device.dto.request;

import com.example.fcm.domain.device.entity.Device;
import com.example.fcm.global.common.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceCreateRequest {

    @NotNull(message = "부모를 지정해주세요.")
    private Long parentId;

    @NotBlank(message = "토큰을 입력해주세요.")
    private String fcmToken;

    public Device toEntity() {
        return Device.builder()
                    .parentId(parentId)
                    .fcmToken(fcmToken)
                    .status(Status.ACTIVE)
                    .build();
    }
}
