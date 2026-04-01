package com.blogapp.teacher.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTeacherRequest {

    private String fullName;
    
    private String mainSubject;

    private String speciality;

    private String bio;

    private String category;

    private String photoUrl;

    private Boolean isActive;
}
