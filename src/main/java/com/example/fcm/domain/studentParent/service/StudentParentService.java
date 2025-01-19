package com.example.fcm.domain.studentParent.service;

import com.example.fcm.domain.member.entity.Member;
import com.example.fcm.domain.member.facade.MemberFacade;
import com.example.fcm.domain.studentParent.dto.request.StudentParentCreateRequest;
import com.example.fcm.domain.studentParent.dto.request.StudentParentUpdateRequest;
import com.example.fcm.domain.studentParent.dto.response.StudentParentResponse;
import com.example.fcm.domain.studentParent.entity.StudentParent;
import com.example.fcm.domain.studentParent.exception.ParentNotFoundException;
import com.example.fcm.domain.studentParent.repository.StudentParentRepository;
import com.example.fcm.domain.studentParent.repository.StudentParentRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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

    @Transactional
    public void updateStudentParent(StudentParentUpdateRequest request, Long parentId) {
        StudentParent studentParent = studentParentRepository.findById(parentId).orElseThrow(ParentNotFoundException::new);

        if (request.getStudentId() != null) {
            Member student = memberFacade.getMember(request.getStudentId());
            studentParent.changeStudent(request.getStudentId());
        }

        if (StringUtils.hasText(request.getName())) {
            studentParent.changeName(request.getName());
        }
    }

    @Transactional
    public void deleteStudentParent(Long parentId) {
        StudentParent studentParent = studentParentRepository.findById(parentId).orElseThrow(ParentNotFoundException::new);

        studentParent.inactive();
    }
}
