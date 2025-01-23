package com.example.fcm.domain.studentParent.repository;

import com.example.fcm.domain.studentParent.dto.response.StudentParentFcmResponse;
import com.example.fcm.domain.studentParent.entity.StudentParent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentParentRepository extends JpaRepository<StudentParent, Long> {

    @Query("select new com.example.fcm.domain.studentParent.dto.response.StudentParentFcmResponse(sp.id, sp.name, m.name, d.fcmToken) " +
            "from StudentParent sp " +
            "join Member m on sp.studentId = m.id " +
            "join Device d on sp.id = d.parentId " +
            "where sp.studentId = :studentId")
    List<StudentParentFcmResponse> findByStudentIdWithFcmToken(Long studentId);
}
