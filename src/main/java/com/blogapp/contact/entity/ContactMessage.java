package com.blogapp.contact.entity;

import com.blogapp.contact.enums.ContactStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "contact_messages")
public class ContactMessage {

    @Id
    private String id;

    private String fullName;
    private String phoneNumber;
    private String emailAddress;
    
    private String subjectId;
    
    private String messageText;

    @Builder.Default
    private ContactStatus status = ContactStatus.UNREAD;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
