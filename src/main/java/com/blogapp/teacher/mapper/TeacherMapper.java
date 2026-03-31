package com.blogapp.teacher.mapper;

import com.blogapp.teacher.dto.response.TeacherResponse;
import com.blogapp.teacher.entity.Teacher;

public class TeacherMapper {

    private TeacherMapper() {}

    public static TeacherResponse toResponse(Teacher teacher) {
        return TeacherResponse.builder()
                .id(teacher.getId())
                .fullName(teacher.getFullName())
                .mainSubject(teacher.getMainSubject())
                .speciality(teacher.getSpeciality())
                .bio(teacher.getBio())
                .photoUrl(teacher.getPhotoUrl())
                .isActive(teacher.isActive())
                .createdAt(teacher.getCreatedAt())
                .updatedAt(teacher.getUpdatedAt())
                .build();
    }
}
