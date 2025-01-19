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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceRepositoryCustom deviceRepositoryCustom;
    private final StudentParentFacade studentParentFacade;

    @Transactional
    public void createDevice(DeviceCreateRequest request) {
        if (!studentParentFacade.existParent(request.getParentId())) {
            throw new ParentNotFoundException();
        }

        Device device = request.toEntity();
        deviceRepository.save(device);
    }

    public DeviceResponse getDevice(Long deviceId) {
        return deviceRepositoryCustom.findDeviceResponseById(deviceId)
                .orElseThrow(DeviceNotFoundException::new);
    }

    public List<DeviceResponse> getDevicesByParent(Long parentId) {
        return deviceRepositoryCustom.findDeviceResponseByParentId(parentId);
    }

    @Transactional
    public void updateFmcToken(DeviceUpdateRequest request, Long deviceId) {
        Device device = deviceRepository.findById(deviceId).orElseThrow(DeviceNotFoundException::new);

        device.changeFmcToken(request.getFmcToken());
    }
}
