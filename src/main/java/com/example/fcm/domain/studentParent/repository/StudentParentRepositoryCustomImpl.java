package com.example.fcm.domain.studentParent.repository;

import com.example.fcm.domain.member.entity.QMember;
import com.example.fcm.domain.studentParent.dto.response.StudentParentResponse;
import com.example.fcm.domain.studentParent.entity.QStudentParent;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StudentParentRepositoryCustomImpl implements StudentParentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private static final QStudentParent studentParent = QStudentParent.studentParent;
    private static final QMember member = QMember.member;

    @Override
    public Optional<StudentParentResponse> findStudentParentResponseById(Long id) {
        return Optional.ofNullable(
                queryFactory
                        .select(Projections.constructor(StudentParentResponse.class,
                                studentParent.id,
                                studentParent.name,
                                member.name,
                                studentParent.createdAt,
                                studentParent.updatedAt))
                        .from(studentParent)
                        .join(member).on(studentParent.studentId.eq(member.id))
                        .where(studentParent.id.eq(id))
                        .fetchOne()
        );
    }

}
