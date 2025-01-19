package com.example.fcm.domain.device.service;

import com.example.fcm.domain.device.dto.request.DeviceCreateRequest;
import com.example.fcm.domain.device.entity.Device;
import com.example.fcm.domain.device.repository.DeviceRepository;
import com.example.fcm.domain.studentParent.exception.ParentNotFoundException;
import com.example.fcm.domain.studentParent.facade.StudentParentFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final StudentParentFacade studentParentFacade;

    @Transactional
    public void createDevice(DeviceCreateRequest request) {
        if (!studentParentFacade.existParent(request.getParentId())) {
            throw new ParentNotFoundException();
        }

        Device device = request.toEntity();
        deviceRepository.save(device);
    }
}
