package com.blogapp.teacher.mapper;

import com.blogapp.teacher.dto.response.TeacherResponse;
import com.blogapp.teacher.entity.Teacher;

public class TeacherMapper {

    private TeacherMapper() {}

    public static TeacherResponse toResponse(Teacher teacher) {
        return TeacherResponse.builder()
                .id(teacher.getId())
                .name(teacher.getName())
                .bio(teacher.getBio())
                .photoUrl(teacher.getPhotoUrl())
                .specialization(teacher.getSpecialization())
                .isActive(teacher.isActive())
                .createdAt(teacher.getCreatedAt())
                .updatedAt(teacher.getUpdatedAt())
                .build();
    }
}
