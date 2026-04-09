package com.blogapp.contact.repository;

import com.blogapp.contact.entity.ContactSubject;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactSubjectRepository extends MongoRepository<ContactSubject, String> {
}
