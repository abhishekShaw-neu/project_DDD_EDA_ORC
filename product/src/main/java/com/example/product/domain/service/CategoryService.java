package com.example.product.domain.service;

import com.example.product.application.dto.CategoryDTO;
import com.example.product.domain.service.command.CategoryCommandService;
import com.example.product.domain.service.query.CategoryQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryCommandService categoryCommandService;
    private final CategoryQueryService categoryQueryService;

    @Autowired
    public CategoryService(CategoryCommandService categoryCommandService, CategoryQueryService categoryQueryService) {
        this.categoryCommandService = categoryCommandService;
        this.categoryQueryService = categoryQueryService;
    }

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        return categoryCommandService.createCategory(categoryDTO).getBody();
    }

    public List<CategoryDTO> getAllCategories() {
        return categoryQueryService.getAllCategories().getBody();
    }

    public CategoryDTO getCategoryById(int id) {
        return categoryQueryService.getCategoryById(id).getBody();
    }

    public CategoryDTO updateCategory(int id, CategoryDTO categoryDTO) {
        return categoryCommandService.updateCategory(id, categoryDTO).getBody();
    }

    public void deleteCategory(int id) {
        categoryCommandService.deleteCategory(id);
    }

    // Additional orchestrated operations can be added here
}
