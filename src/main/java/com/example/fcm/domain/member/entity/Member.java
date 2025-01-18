package com.example.fcm.domain.member.entity;

import com.example.fcm.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @Builder
    public Member(String name) {
        this.name = name;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void inactive() {
        this.status = MemberStatus.INACTIVE;
    }
}
