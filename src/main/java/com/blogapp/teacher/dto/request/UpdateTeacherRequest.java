package com.blogapp.teacher.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTeacherRequest {

    private String name;

    private String bio;

    private String photoUrl;

    private String specialization;

    private Boolean isActive;
}
