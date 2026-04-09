package com.blogapp.demo.service.impl;

import com.blogapp.common.exception.ResourceNotFoundException;
import com.blogapp.demo.dto.request.CreateBoardRequest;
import com.blogapp.demo.dto.request.CreateGradeRequest;
import com.blogapp.demo.dto.response.BoardResponse;
import com.blogapp.demo.dto.response.GradeResponse;
import com.blogapp.demo.entity.Board;
import com.blogapp.demo.entity.Grade;
import com.blogapp.demo.mapper.DemoMapper;
import com.blogapp.demo.repository.BoardRepository;
import com.blogapp.demo.repository.GradeRepository;
import com.blogapp.demo.service.DemoSettingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DemoSettingsServiceImpl implements DemoSettingsService {

    private final BoardRepository boardRepository;
    private final GradeRepository gradeRepository;
    private final DemoMapper demoMapper;

    @Override
    @Transactional
    public BoardResponse createBoard(CreateBoardRequest request) {
        Board board = Board.builder()
                .name(request.getName())
                .build();
        return demoMapper.toBoardResponse(boardRepository.save(board));
    }

    @Override
    public List<BoardResponse> getAllBoards() {
        return boardRepository.findAll().stream()
                .map(demoMapper::toBoardResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteBoard(String id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found"));
        boardRepository.delete(board);
    }

    @Override
    @Transactional
    public GradeResponse createGrade(CreateGradeRequest request) {
        Grade grade = Grade.builder()
                .name(request.getName())
                .build();
        return demoMapper.toGradeResponse(gradeRepository.save(grade));
    }

    @Override
    public List<GradeResponse> getAllGrades() {
        return gradeRepository.findAll().stream()
                .map(demoMapper::toGradeResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteGrade(String id) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grade not found"));
        gradeRepository.delete(grade);
    }
}
