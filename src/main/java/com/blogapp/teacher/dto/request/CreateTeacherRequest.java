package com.blogapp.teacher.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTeacherRequest {

    @NotBlank(message = "Teacher name is required")
    private String name;

    private String bio;

    private String photoUrl;

    private String specialization;
}
