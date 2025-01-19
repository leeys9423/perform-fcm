package com.example.fcm.domain.studentParent.entity;

import com.example.fcm.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "student_parents")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class StudentParent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;
    private String name;

    @Builder
    public StudentParent(Long studentId, String name) {
        this.studentId = studentId;
        this.name = name;
    }

    public void changeStudent(Long studentId) {
        this.studentId = studentId;
    }

    public void changeName(String name) {
        this.name = name;
    }
}
