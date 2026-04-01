package com.blogapp.teacher.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTeacherRequest {

    @NotBlank(message = "Teacher full name is required")
    private String fullName;
    
    private String mainSubject;

    private String speciality;

    private String bio;

    private String category;

    private String photoUrl;
}
