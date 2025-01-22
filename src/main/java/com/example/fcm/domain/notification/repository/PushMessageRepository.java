package com.example.fcm.domain.notification.repository;

import com.example.fcm.domain.notification.entity.PushMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PushMessageRepository extends JpaRepository<PushMessage, Long> {

}
