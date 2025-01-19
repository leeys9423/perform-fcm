package com.example.fcm.domain.device.controller;

import com.example.fcm.domain.device.dto.request.DeviceCreateRequest;
import com.example.fcm.domain.device.service.DeviceService;
import com.example.fcm.global.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/devices")
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping
    public ResponseEntity<BaseResponse<Void>> createDevice(@RequestBody @Valid DeviceCreateRequest request) {
        deviceService.createDevice(request);
        return ResponseEntity.ok(BaseResponse.of(null, "기기 등록 성공"));
    }

}
