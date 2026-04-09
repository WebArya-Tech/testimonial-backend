package com.blogapp.demo.repository;

import com.blogapp.demo.entity.Board;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends MongoRepository<Board, String> {
}
