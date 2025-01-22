package com.example.fcm.domain.studentParent.facade;

import com.example.fcm.domain.studentParent.dto.response.StudentParentFcmResponse;
import com.example.fcm.domain.studentParent.entity.StudentParent;
import com.example.fcm.domain.studentParent.service.StudentParentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StudentParentFacade {

    private final StudentParentService studentParentService;

    public StudentParent getParent(Long parentId) {
        return studentParentService.getParent(parentId);
    }

    public boolean existParent(Long parentId) {
        return studentParentService.existParent(parentId);
    }

    public List<StudentParentFcmResponse> getParentsFcmTokensByStudentId(Long studentId) {
        return studentParentService.getParentsFcmTokensByStudentId(studentId);
    }
}
