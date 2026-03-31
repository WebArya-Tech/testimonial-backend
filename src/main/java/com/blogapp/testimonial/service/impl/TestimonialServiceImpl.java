package com.blogapp.testimonial.service.impl;

import com.blogapp.common.dto.PageResponse;
import com.blogapp.common.exception.ResourceNotFoundException;
import com.blogapp.testimonial.dto.request.SubmitTestimonialRequest;
import com.blogapp.testimonial.dto.response.TestimonialResponse;
import com.blogapp.testimonial.entity.Testimonial;
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
    private final CloudinaryService cloudinaryService;

    @Override
    public TestimonialResponse submit(SubmitTestimonialRequest request) {
        Testimonial testimonial = Testimonial.builder()
                .text(request.getText())
                .mediaUrl(request.getMediaUrl())
                .build();

        testimonial = testimonialRepository.save(testimonial);
        return TestimonialMapper.toResponse(testimonial);
    }

    @Override
    public TestimonialResponse update(String id, SubmitTestimonialRequest request) {
        Testimonial testimonial = getTestimonialEntity(id);

        testimonial.setText(request.getText());
        testimonial.setMediaUrl(request.getMediaUrl());

        testimonial = testimonialRepository.save(testimonial);
        return TestimonialMapper.toResponse(testimonial);
    }

    @Override
    public List<TestimonialResponse> getAll() {
        return testimonialRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(TestimonialMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TestimonialResponse> getPrimaryTestimonials() {
        return testimonialRepository.findByIsPrimaryTrue()
                .stream()
                .map(TestimonialMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<TestimonialResponse> getPaginated(int page, int size) {
        Page<Testimonial> testimonialPage = testimonialRepository.findAllByOrderByCreatedAtDesc(
                PageRequest.of(page, size)
        );

        List<TestimonialResponse> content = testimonialPage.getContent().stream()
                .map(TestimonialMapper::toResponse)
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

        // Clear all existing primary testimonials globally
        List<Testimonial> existingPrimary = testimonialRepository.findByIsPrimaryTrue();
        for (Testimonial existing : existingPrimary) {
            existing.setPrimary(false);
            testimonialRepository.save(existing);
        }

        // Set this one as primary
        testimonial.setPrimary(true);
        testimonial = testimonialRepository.save(testimonial);

        return TestimonialMapper.toResponse(testimonial);
    }

    @Override
    public void delete(String id) {
        Testimonial testimonial = getTestimonialEntity(id);
        
        // Prevent Cloudinary cost-leaks: Wiping a Video Testimonial should physically delete the origin payload too!
        if (testimonial.getMediaUrl() != null 
                && testimonial.getMediaUrl().contains("cloudinary.com")) {
            cloudinaryService.deleteMediaByUrl(testimonial.getMediaUrl());
        }
        
        testimonialRepository.delete(testimonial);
    }

    private Testimonial getTestimonialEntity(String id) {
        return testimonialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Testimonial not found with id: " + id));
    }
}
