package com.blogapp.contact.dto.response;

import com.blogapp.contact.enums.ContactStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ContactMessageResponse {
    private String id;

    private String fullName;
    private String phoneNumber;
    private String emailAddress;
    
    private String subjectId;
    private SubjectResponse subject;
    
    private String messageText;

    private ContactStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
