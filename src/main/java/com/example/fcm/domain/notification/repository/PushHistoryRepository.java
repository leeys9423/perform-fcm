package com.example.fcm.domain.notification.repository;

import com.example.fcm.domain.notification.entity.PushHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PushHistoryRepository extends JpaRepository<PushHistory, Long> {

}
