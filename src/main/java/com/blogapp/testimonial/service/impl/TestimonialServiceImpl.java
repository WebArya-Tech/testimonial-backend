package com.blogapp.testimonial.service.impl;

import com.blogapp.common.dto.PageResponse;
import com.blogapp.common.exception.ResourceNotFoundException;
import com.blogapp.teacher.entity.Teacher;
import com.blogapp.teacher.service.TeacherService;
import com.blogapp.testimonial.dto.request.SubmitTestimonialRequest;
import com.blogapp.testimonial.dto.response.TestimonialResponse;
import com.blogapp.testimonial.entity.Testimonial;
import com.blogapp.testimonial.enums.TestimonialType;
import com.blogapp.testimonial.mapper.TestimonialMapper;
import com.blogapp.testimonial.repository.TestimonialRepository;
import com.blogapp.testimonial.service.TestimonialService;
import com.blogapp.media.service.CloudinaryService;
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
    private final CloudinaryService cloudinaryService;

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
    public TestimonialResponse update(String id, SubmitTestimonialRequest request) {
        Testimonial testimonial = getTestimonialEntity(id);
        Teacher teacher = teacherService.getTeacherEntity(request.getTeacherId());

        testimonial.setTeacherId(request.getTeacherId());
        testimonial.setReviewerName(request.getReviewerName());
        testimonial.setReviewerEmail(request.getReviewerEmail());
        testimonial.setContent(request.getContent());
        testimonial.setType(request.getType());

        testimonial = testimonialRepository.save(testimonial);
        return TestimonialMapper.toResponse(testimonial, teacher.getName());
    }

    @Override
    public List<TestimonialResponse> getByTeacher(String teacherId) {
        Teacher teacher = teacherService.getTeacherEntity(teacherId);

        return testimonialRepository.findByTeacherId(teacherId)
                .stream()
                .map(t -> TestimonialMapper.toResponse(t, teacher.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<TestimonialResponse> getAll() {
        return testimonialRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(t -> {
                    String teacherName = getTeacherNameSafe(t.getTeacherId());
                    return TestimonialMapper.toResponse(t, teacherName);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<TestimonialResponse> getPrimaryTestimonials() {
        return testimonialRepository.findByIsPrimaryTrue()
                .stream()
                .map(t -> {
                    String teacherName = getTeacherNameSafe(t.getTeacherId());
                    return TestimonialMapper.toResponse(t, teacherName);
                })
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<TestimonialResponse> getPaginated(int page, int size) {
        Page<Testimonial> testimonialPage = testimonialRepository.findAllByOrderByCreatedAtDesc(
                PageRequest.of(page, size)
        );

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
        testimonial = testimonialRepository.save(testimonial);

        String teacherName = getTeacherNameSafe(testimonial.getTeacherId());
        return TestimonialMapper.toResponse(testimonial, teacherName);
    }

    @Override
    public void delete(String id) {
        Testimonial testimonial = getTestimonialEntity(id);
        
        // Prevent Cloudinary cost-leaks: Wiping a Video Testimonial should physically delete the origin payload too!
        if (testimonial.getType() == TestimonialType.URL 
                && testimonial.getContent() != null 
                && testimonial.getContent().contains("cloudinary.com")) {
            cloudinaryService.deleteMediaByUrl(testimonial.getContent());
        }
        
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
