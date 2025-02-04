package com.example.fcm.domain.device.controller;

import com.example.fcm.domain.device.dto.request.DeviceCreateRequest;
import com.example.fcm.domain.device.dto.request.DeviceUpdateRequest;
import com.example.fcm.domain.device.dto.response.DeviceResponse;
import com.example.fcm.domain.device.service.DeviceService;
import com.example.fcm.global.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{deviceId}")
    public ResponseEntity<BaseResponse<DeviceResponse>> getDevice(@PathVariable Long deviceId) {
        return ResponseEntity.ok(BaseResponse.of(deviceService.getDeviceResponse(deviceId), "기기 조회 성공"));
    }

    @GetMapping("/parents/{parentId}")
    public ResponseEntity<BaseResponse<List<DeviceResponse>>> getDevicesByParentId(@PathVariable Long parentId) {
        return ResponseEntity.ok(BaseResponse.of(deviceService.getDevicesByParent(parentId), "기기 조회 성공"));
    }

    @PutMapping("/{deviceId}")
    public ResponseEntity<BaseResponse<Void>> updateFcmToken(@RequestBody @Valid DeviceUpdateRequest request, @PathVariable Long deviceId) {
        deviceService.updateFmcToken(request, deviceId);
        return ResponseEntity.ok(BaseResponse.of(null, "기기 수정 성공"));
    }

    @DeleteMapping("/{deviceId}")
    public ResponseEntity<BaseResponse<Void>> deleteDevice(@PathVariable Long deviceId) {
        deviceService.deleteDevice(deviceId);
        return ResponseEntity.ok(BaseResponse.of(null, "기기 삭제 성공"));
    }
}
