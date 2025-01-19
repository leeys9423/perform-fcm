package com.example.fcm.domain.studentParent.dto.request;

import com.example.fcm.domain.studentParent.entity.StudentParent;
import com.example.fcm.global.common.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentParentCreateRequest {

    @NotNull(message = "학생을 지정해주세요.")
    private Long studentId;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    public StudentParent toEntity() {
        return StudentParent.builder()
                .studentId(studentId)
                .name(name)
                .status(Status.ACTIVE)
                .build();
    }
}
