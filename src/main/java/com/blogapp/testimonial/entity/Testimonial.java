package com.blogapp.testimonial.entity;

import com.blogapp.testimonial.enums.TestimonialStatus;
import com.blogapp.testimonial.enums.TestimonialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "testimonials")
public class Testimonial {

    @Id
    private String id;

    @Indexed
    private String teacherId;

    private String reviewerName;

    private String reviewerEmail;

    /**
     * Holds either plain text review or a Cloudinary URL
     * (video/image/audio). The 'type' field tells the frontend
     * how to render this content.
     */
    private String content;

    @Indexed
    @Builder.Default
    private TestimonialType type = TestimonialType.TEXT;

    @Indexed
    @Builder.Default
    private TestimonialStatus status = TestimonialStatus.PENDING;

    @Builder.Default
    private boolean isPrimary = false;

    private String rejectionReason;

    private String approvedByAdminId;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
