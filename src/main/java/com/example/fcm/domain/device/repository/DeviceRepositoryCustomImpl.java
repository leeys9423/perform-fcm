package com.example.fcm.domain.device.repository;

import com.example.fcm.domain.device.dto.response.DeviceResponse;
import com.example.fcm.domain.device.entity.QDevice;
import com.example.fcm.domain.studentParent.entity.QStudentParent;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DeviceRepositoryCustomImpl implements DeviceRepositoryCustom {
    private static final QDevice device = QDevice.device;
    private static final QStudentParent studentParent = QStudentParent.studentParent;

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<DeviceResponse> findDeviceResponseById(Long id) {
        return Optional.ofNullable(
                queryFactory
                        .select(Projections.constructor(DeviceResponse.class,
                                device.id,
                                studentParent.name,
                                device.fcmToken,
                                device.status,
                                device.createdAt,
                                device.updatedAt))
                        .from(device)
                        .join(studentParent).on(device.parentId.eq(studentParent.id))
                        .where(device.id.eq(id))
                        .fetchOne()
        );
    }

    @Override
    public List<DeviceResponse> findDeviceResponseByParentId(Long id) {
        return queryFactory
                .select(Projections.constructor(DeviceResponse.class,
                        device.id,
                        studentParent.name,
                        device.fcmToken,
                        device.status,
                        device.createdAt,
                        device.updatedAt))
                .from(device)
                .join(studentParent).on(device.parentId.eq(studentParent.id))
                .where(studentParent.id.eq(id))
                .fetch();
    }
}
