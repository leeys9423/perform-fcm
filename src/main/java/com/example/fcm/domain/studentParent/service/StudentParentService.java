package com.example.fcm.domain.studentParent.service;

import com.example.fcm.domain.member.facade.MemberFacade;
import com.example.fcm.domain.studentParent.dto.request.StudentParentCreateRequest;
import com.example.fcm.domain.studentParent.entity.StudentParent;
import com.example.fcm.domain.studentParent.repository.StudentParentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentParentService {

    private final MemberFacade memberFacade;
    private final StudentParentRepository studentParentRepository;

    public Long createStudentParent(StudentParentCreateRequest request) {
        memberFacade.getMember(request.getStudentId());

        StudentParent studentParent = request.toEntity();
        return studentParentRepository.save(studentParent).getId();
    }
}
