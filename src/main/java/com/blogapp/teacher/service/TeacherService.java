package com.blogapp.teacher.service;

import com.blogapp.common.dto.PageResponse;
import com.blogapp.teacher.dto.request.CreateTeacherRequest;
import com.blogapp.teacher.dto.request.UpdateTeacherRequest;
import com.blogapp.teacher.dto.response.TeacherResponse;
import com.blogapp.teacher.entity.Teacher;

import java.util.List;

public interface TeacherService {

    TeacherResponse create(CreateTeacherRequest request);

    TeacherResponse getById(String id);

    PageResponse<TeacherResponse> getAll(int page, int size);

    List<TeacherResponse> getAllActive();

    TeacherResponse update(String id, UpdateTeacherRequest request);

    void delete(String id);

    Teacher getTeacherEntity(String id);
}
