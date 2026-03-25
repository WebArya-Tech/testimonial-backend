package com.blogapp.testimonial.service.impl;

import com.blogapp.common.dto.PageResponse;
import com.blogapp.common.exception.ResourceNotFoundException;
import com.blogapp.teacher.entity.Teacher;
import com.blogapp.teacher.service.TeacherService;
import com.blogapp.testimonial.dto.request.SubmitTestimonialRequest;
import com.blogapp.testimonial.dto.response.TestimonialResponse;
import com.blogapp.testimonial.entity.Testimonial;
import com.blogapp.testimonial.enums.TestimonialStatus;
import com.blogapp.testimonial.mapper.TestimonialMapper;
import com.blogapp.testimonial.repository.TestimonialRepository;
import com.blogapp.testimonial.service.TestimonialService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestimonialServiceImpl implements TestimonialService {

    private final TestimonialRepository testimonialRepository;
    private final TeacherService teacherService;

    @Override
    public TestimonialResponse submit(SubmitTestimonialRequest request) {
        // Validate teacher exists
        Teacher teacher = teacherService.getTeacherEntity(request.getTeacherId());

        Testimonial testimonial = Testimonial.builder()
                .teacherId(request.getTeacherId())
                .reviewerName(request.getReviewerName())
                .reviewerEmail(request.getReviewerEmail())
                .content(request.getContent())
                .type(request.getType())
                .build();

        testimonial = testimonialRepository.save(testimonial);
        return TestimonialMapper.toResponse(testimonial, teacher.getName());
    }

    @Override
    public List<TestimonialResponse> getApprovedByTeacher(String teacherId) {
        // Validate teacher exists
        Teacher teacher = teacherService.getTeacherEntity(teacherId);

        return testimonialRepository.findByTeacherIdAndStatus(teacherId, TestimonialStatus.APPROVED)
                .stream()
                .map(t -> TestimonialMapper.toResponse(t, teacher.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<TestimonialResponse> getAllApproved() {
        return testimonialRepository.findByStatusOrderByCreatedAtDesc(TestimonialStatus.APPROVED)
                .stream()
                .map(t -> {
                    String teacherName = getTeacherNameSafe(t.getTeacherId());
                    return TestimonialMapper.toResponse(t, teacherName);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<TestimonialResponse> getPrimaryTestimonials() {
        return testimonialRepository.findByIsPrimaryTrueAndStatus(TestimonialStatus.APPROVED)
                .stream()
                .map(t -> {
                    String teacherName = getTeacherNameSafe(t.getTeacherId());
                    return TestimonialMapper.toResponse(t, teacherName);
                })
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<TestimonialResponse> getAll(String status, int page, int size) {
        Page<Testimonial> testimonialPage;

        if (status != null && !status.isBlank()) {
            TestimonialStatus testimonialStatus = TestimonialStatus.valueOf(status.toUpperCase());
            testimonialPage = testimonialRepository.findByStatus(testimonialStatus,
                    PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        } else {
            testimonialPage = testimonialRepository.findAll(
                    PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        }

        List<TestimonialResponse> content = testimonialPage.getContent().stream()
                .map(t -> {
                    String teacherName = getTeacherNameSafe(t.getTeacherId());
                    return TestimonialMapper.toResponse(t, teacherName);
                })
                .collect(Collectors.toList());

        return PageResponse.<TestimonialResponse>builder()
                .content(content)
                .page(testimonialPage.getNumber())
                .size(testimonialPage.getSize())
                .totalElements(testimonialPage.getTotalElements())
                .totalPages(testimonialPage.getTotalPages())
                .last(testimonialPage.isLast())
                .build();
    }

    @Override
    public TestimonialResponse approve(String id, String adminId) {
        Testimonial testimonial = getTestimonialEntity(id);
        testimonial.setStatus(TestimonialStatus.APPROVED);
        testimonial.setApprovedByAdminId(adminId);
        testimonial.setRejectionReason(null);
        testimonial = testimonialRepository.save(testimonial);

        String teacherName = getTeacherNameSafe(testimonial.getTeacherId());
        return TestimonialMapper.toResponse(testimonial, teacherName);
    }

    @Override
    public TestimonialResponse reject(String id, String reason) {
        Testimonial testimonial = getTestimonialEntity(id);
        testimonial.setStatus(TestimonialStatus.REJECTED);
        testimonial.setRejectionReason(reason);
        testimonial = testimonialRepository.save(testimonial);

        String teacherName = getTeacherNameSafe(testimonial.getTeacherId());
        return TestimonialMapper.toResponse(testimonial, teacherName);
    }

    @Override
    public TestimonialResponse setPrimary(String id) {
        Testimonial testimonial = getTestimonialEntity(id);

        // Clear existing primary for the same teacher
        List<Testimonial> existingPrimary = testimonialRepository
                .findByTeacherIdAndIsPrimaryTrue(testimonial.getTeacherId());
        for (Testimonial existing : existingPrimary) {
            existing.setPrimary(false);
            testimonialRepository.save(existing);
        }

        // Set this one as primary
        testimonial.setPrimary(true);
        // Auto-approve if still pending
        if (testimonial.getStatus() != TestimonialStatus.APPROVED) {
            testimonial.setStatus(TestimonialStatus.APPROVED);
        }
        testimonial = testimonialRepository.save(testimonial);

        String teacherName = getTeacherNameSafe(testimonial.getTeacherId());
        return TestimonialMapper.toResponse(testimonial, teacherName);
    }

    @Override
    public void delete(String id) {
        Testimonial testimonial = getTestimonialEntity(id);
        testimonialRepository.delete(testimonial);
    }

    private Testimonial getTestimonialEntity(String id) {
        return testimonialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Testimonial not found with id: " + id));
    }

    private String getTeacherNameSafe(String teacherId) {
        try {
            return teacherService.getTeacherEntity(teacherId).getName();
        } catch (ResourceNotFoundException e) {
            return "Unknown Teacher";
        }
    }
}
