package com.example.fcm.domain.studentParent.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentParentUpdateRequest {

    private Long studentId;
    private String name;
}
