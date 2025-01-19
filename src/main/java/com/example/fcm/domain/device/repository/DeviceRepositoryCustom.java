package com.example.fcm.domain.device.repository;

import com.example.fcm.domain.device.dto.response.DeviceResponse;

import java.util.List;
import java.util.Optional;

public interface DeviceRepositoryCustom {

    Optional<DeviceResponse> findDeviceResponseById(Long id);

    List<DeviceResponse> findDeviceResponseByParentId(Long id);
}
