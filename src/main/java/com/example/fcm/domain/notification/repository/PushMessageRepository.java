package com.example.fcm.domain.notification.repository;

import com.example.fcm.domain.notification.entity.MessageStatus;
import com.example.fcm.domain.notification.entity.NotificationType;
import com.example.fcm.domain.notification.entity.PushMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PushMessageRepository extends JpaRepository<PushMessage, Long> {

    List<PushMessage> findByStatusAndNextAttemptTimeBefore(MessageStatus status, LocalDateTime nextAttemptTime);

    @Modifying
    @Query("UPDATE PushMessage p SET p.status = :newStatus " +
            "WHERE p.status IN (:statuses) " +
            "AND p.createdAt < :cutoffTime")
    int updateStatusForStalledMessages(
            @Param("newStatus") MessageStatus newStatus,
            @Param("statuses") List<MessageStatus> statuses,
            @Param("cutoffTime") LocalDateTime cutoffTime);
}
