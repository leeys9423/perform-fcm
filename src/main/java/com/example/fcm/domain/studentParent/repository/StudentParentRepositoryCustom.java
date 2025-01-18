package com.example.fcm.domain.studentParent.repository;

import com.example.fcm.domain.studentParent.dto.response.StudentParentResponse;

import java.util.Optional;

public interface StudentParentRepositoryCustom {

    Optional<StudentParentResponse> findStudentParentResponseById(Long id);
}
