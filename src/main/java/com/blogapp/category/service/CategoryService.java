package com.blogapp.category.service;

import com.blogapp.category.dto.CategoryRequest;
import com.blogapp.category.dto.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest request);
    CategoryResponse updateCategory(String id, CategoryRequest request);
    void deleteCategory(String id);
    CategoryResponse getCategoryById(String id);
    CategoryResponse getCategoryBySlug(String slug);
    List<CategoryResponse> getAllCategories();
}
