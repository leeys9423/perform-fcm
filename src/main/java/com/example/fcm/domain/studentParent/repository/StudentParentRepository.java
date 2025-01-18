package com.example.fcm.domain.studentParent.repository;

import com.example.fcm.domain.studentParent.entity.StudentParent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentParentRepository extends JpaRepository<StudentParent, Long> {

}
