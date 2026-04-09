package com.blogapp.contact.mapper;

import com.blogapp.contact.dto.response.ContactMessageResponse;
import com.blogapp.contact.dto.response.SubjectResponse;
import com.blogapp.contact.entity.ContactMessage;
import com.blogapp.contact.entity.ContactSubject;
import org.springframework.stereotype.Component;

@Component
public class ContactMapper {

    public SubjectResponse toSubjectResponse(ContactSubject subject) {
        if (subject == null) return null;
        return SubjectResponse.builder()
                .id(subject.getId())
                .name(subject.getName())
                .createdAt(subject.getCreatedAt())
                .build();
    }

    public ContactMessageResponse toContactMessageResponse(ContactMessage message, ContactSubject subject) {
        if (message == null) return null;
        return ContactMessageResponse.builder()
                .id(message.getId())
                .fullName(message.getFullName())
                .phoneNumber(message.getPhoneNumber())
                .emailAddress(message.getEmailAddress())
                .subjectId(message.getSubjectId())
                .subject(toSubjectResponse(subject))
                .messageText(message.getMessageText())
                .status(message.getStatus())
                .createdAt(message.getCreatedAt())
                .updatedAt(message.getUpdatedAt())
                .build();
    }
}
