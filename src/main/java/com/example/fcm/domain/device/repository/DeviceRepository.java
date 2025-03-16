package com.example.fcm.domain.device.repository;

import com.example.fcm.domain.device.entity.Device;
import com.example.fcm.global.common.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    @Query("SELECT d.fcmToken FROM Device d WHERE d.parentId = :parentId AND d.status = 'ACTIVE' ORDER BY d.updatedAt DESC LIMIT 1")
    String findLatestFcmTokenByParentId(Long parentId);
}
