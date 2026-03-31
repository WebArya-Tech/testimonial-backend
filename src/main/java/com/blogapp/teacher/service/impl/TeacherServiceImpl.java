package com.blogapp.teacher.service.impl;

import com.blogapp.common.dto.PageResponse;
import com.blogapp.common.exception.ResourceNotFoundException;
import com.blogapp.teacher.dto.request.CreateTeacherRequest;
import com.blogapp.teacher.dto.request.UpdateTeacherRequest;
import com.blogapp.teacher.dto.response.TeacherResponse;
import com.blogapp.teacher.entity.Teacher;
import com.blogapp.teacher.mapper.TeacherMapper;
import com.blogapp.teacher.repository.TeacherRepository;
import com.blogapp.teacher.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;

    @Override
    public TeacherResponse create(CreateTeacherRequest request) {
        Teacher teacher = Teacher.builder()
                .fullName(request.getFullName())
                .mainSubject(request.getMainSubject())
                .speciality(request.getSpeciality())
                .bio(request.getBio())
                .photoUrl(request.getPhotoUrl())
                .build();

        teacher = teacherRepository.save(teacher);
        return TeacherMapper.toResponse(teacher);
    }

    @Override
    public TeacherResponse getById(String id) {
        Teacher teacher = getTeacherEntity(id);
        return TeacherMapper.toResponse(teacher);
    }

    @Override
    public PageResponse<TeacherResponse> getAll(int page, int size) {
        Page<Teacher> teacherPage = teacherRepository.findAll(
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));

        List<TeacherResponse> content = teacherPage.getContent().stream()
                .map(TeacherMapper::toResponse)
                .collect(Collectors.toList());

        return PageResponse.<TeacherResponse>builder()
                .content(content)
                .page(teacherPage.getNumber())
                .size(teacherPage.getSize())
                .totalElements(teacherPage.getTotalElements())
                .totalPages(teacherPage.getTotalPages())
                .last(teacherPage.isLast())
                .build();
    }

    @Override
    public List<TeacherResponse> getAllActive() {
        return teacherRepository.findByIsActiveTrue().stream()
                .map(TeacherMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TeacherResponse update(String id, UpdateTeacherRequest request) {
        Teacher teacher = getTeacherEntity(id);

        if (request.getFullName() != null) teacher.setFullName(request.getFullName());
        if (request.getMainSubject() != null) teacher.setMainSubject(request.getMainSubject());
        if (request.getSpeciality() != null) teacher.setSpeciality(request.getSpeciality());
        if (request.getBio() != null) teacher.setBio(request.getBio());
        if (request.getPhotoUrl() != null) teacher.setPhotoUrl(request.getPhotoUrl());
        if (request.getIsActive() != null) teacher.setActive(request.getIsActive());

        teacher = teacherRepository.save(teacher);
        return TeacherMapper.toResponse(teacher);
    }

    @Override
    public void delete(String id) {
        Teacher teacher = getTeacherEntity(id);
        teacherRepository.delete(teacher);
    }

    @Override
    public Teacher getTeacherEntity(String id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + id));
    }
}
