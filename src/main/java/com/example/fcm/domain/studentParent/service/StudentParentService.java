package com.example.fcm.domain.studentParent.service;

import com.example.fcm.domain.member.facade.MemberFacade;
import com.example.fcm.domain.studentParent.dto.request.StudentParentCreateRequest;
import com.example.fcm.domain.studentParent.dto.response.StudentParentResponse;
import com.example.fcm.domain.studentParent.entity.StudentParent;
import com.example.fcm.domain.studentParent.exception.ParentNotFoundException;
import com.example.fcm.domain.studentParent.repository.StudentParentRepository;
import com.example.fcm.domain.studentParent.repository.StudentParentRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentParentService {

    private final MemberFacade memberFacade;
    private final StudentParentRepository studentParentRepository;
    private final StudentParentRepositoryCustom studentParentRepositoryCustom;

    @Transactional
    public Long createStudentParent(StudentParentCreateRequest request) {
        memberFacade.getMember(request.getStudentId());

        StudentParent studentParent = request.toEntity();
        return studentParentRepository.save(studentParent).getId();
    }

    public StudentParentResponse getStudentParent(Long parentId) {
        return studentParentRepositoryCustom.findStudentParentResponseById(parentId)
                .orElseThrow(ParentNotFoundException::new);
    }
}
