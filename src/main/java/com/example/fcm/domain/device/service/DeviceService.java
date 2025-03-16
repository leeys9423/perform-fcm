package com.example.fcm.domain.device.service;

import com.example.fcm.domain.device.dto.request.DeviceCreateRequest;
import com.example.fcm.domain.device.dto.request.DeviceUpdateRequest;
import com.example.fcm.domain.device.dto.response.DeviceResponse;
import com.example.fcm.domain.device.entity.Device;
import com.example.fcm.domain.device.exception.DeviceNotFoundException;
import com.example.fcm.domain.device.repository.DeviceRepository;
import com.example.fcm.domain.device.repository.DeviceRepositoryCustom;
import com.example.fcm.domain.studentParent.exception.ParentNotFoundException;
import com.example.fcm.domain.studentParent.facade.StudentParentFacade;
import com.example.fcm.global.common.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceRepositoryCustom deviceRepositoryCustom;
    private final StudentParentFacade studentParentFacade;

    public Device getDevice(Long deviceId) {
        return deviceRepository.findById(deviceId).orElseThrow(DeviceNotFoundException::new);
    }

    @Transactional
    public void createDevice(DeviceCreateRequest request) {
        if (!studentParentFacade.existParent(request.getParentId())) {
            throw new ParentNotFoundException();
        }

        Device device = request.toEntity();
        deviceRepository.save(device);
    }

    public DeviceResponse getDeviceResponse(Long deviceId) {
        return deviceRepositoryCustom.findDeviceResponseById(deviceId)
                .orElseThrow(DeviceNotFoundException::new);
    }

    public List<DeviceResponse> getDevicesByParent(Long parentId) {
        return deviceRepositoryCustom.findDeviceResponseByParentId(parentId);
    }

    @Transactional
    public void updateFmcToken(DeviceUpdateRequest request, Long deviceId) {
        Device device = getDevice(deviceId);

        device.changeFmcToken(request.getFmcToken());
    }

    @Transactional
    public void deleteDevice(Long deviceId) {
        Device device = getDevice(deviceId);

        device.inactive();
    }

    @Transactional(readOnly = true)
    public String getLatestFcmTokenByParentId(Long parentId) {
        try {
            // 가장 최근에 업데이트된 활성 디바이스의 FCM 토큰 조회
            String fcmToken = deviceRepository.findLatestFcmTokenByParentId(parentId);

            if (fcmToken == null || fcmToken.isEmpty()) {
                log.warn("부모 ID {}에 대한 FCM 토큰 없음", parentId);
            }

            return fcmToken;

        } catch (Exception e) {
            log.error("FCM 토큰 조회 실패: 부모ID={}", parentId, e);
            return null;
        }
    }
}
