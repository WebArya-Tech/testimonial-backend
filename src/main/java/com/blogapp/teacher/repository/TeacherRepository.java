package com.blogapp.teacher.repository;

import com.blogapp.teacher.entity.Teacher;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TeacherRepository extends MongoRepository<Teacher, String> {

    List<Teacher> findByIsActiveTrue();
}
