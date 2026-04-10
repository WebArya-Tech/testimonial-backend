package com.blogapp.demo.service;

import com.blogapp.demo.dto.request.CreateBoardRequest;
import com.blogapp.demo.dto.request.CreateGradeRequest;
import com.blogapp.demo.dto.response.BoardResponse;
import com.blogapp.demo.dto.response.GradeResponse;

import java.util.List;

public interface DemoSettingsService {

    BoardResponse createBoard(CreateBoardRequest request);
    List<BoardResponse> getAllBoards();
    void deleteBoard(String id);

    GradeResponse createGrade(CreateGradeRequest request);
    List<GradeResponse> getAllGrades();
    void deleteGrade(String id);
}
