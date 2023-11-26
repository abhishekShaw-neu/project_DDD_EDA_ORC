package com.example.product.domain.service.command;

import com.example.product.application.dto.CategoryDTO;
import com.example.product.domain.model.Category;
import com.example.product.domain.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CategoryCommandService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoryCommandService(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    // Create operation
    public ResponseEntity<CategoryDTO> createCategory(CategoryDTO categoryDTO) {
        // You may want to add validation or other business logic here
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category savedCategory = categoryRepository.save(category);
        CategoryDTO savedCategoryDTO = modelMapper.map(savedCategory, CategoryDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategoryDTO);
    }

    // Update operation
    public ResponseEntity<CategoryDTO> updateCategory(int id, CategoryDTO categoryDTO) {
        if (categoryRepository.existsById(id)) {
            // You may want to add validation or other business logic here
            categoryDTO.setId(id); // Set the ID from the path variable
            Category category = modelMapper.map(categoryDTO, Category.class);
            Category updatedCategory = categoryRepository.save(category);
            CategoryDTO updatedCategoryDTO = modelMapper.map(updatedCategory, CategoryDTO.class);
            return ResponseEntity.ok(updatedCategoryDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Delete operation
    public ResponseEntity<Void> deleteCategory(int id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
