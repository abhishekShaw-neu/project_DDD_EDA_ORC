package com.example.product.domain.service.event;


import com.example.product.application.dto.CategoryDTO;
import com.example.product.application.dto.ProductResponseDTO;
import com.example.product.domain.event.ProductCreatedEvent;
import com.example.product.domain.event.ProductDeletedEvent;
import com.example.product.domain.event.ProductUpdatedEvent;
import com.example.product.domain.model.Category;
import com.example.product.domain.model.Product;
import com.example.product.domain.repository.CategoryRepository;
import com.example.product.domain.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductEventService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductEventService(
            CategoryRepository categoryRepository,
            ProductRepository productRepository,
            ModelMapper modelMapper
    ) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<ProductResponseDTO> handleProductCreatedEvent(ProductCreatedEvent event) {
        // Retrieve the product from the repository
        Optional<Product> productOptional = productRepository.findById(event.getProductId());

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            ProductResponseDTO responseDTO = modelMapper.map(product, ProductResponseDTO.class);

            // Check if product has a valid category before mapping
            if (product.getCategory() != null) {
                Category category = categoryRepository.findById(product.getCategory()).orElse(null);

                if (category != null) {
                    responseDTO.setCategory(modelMapper.map(category, CategoryDTO.class));
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ProductResponseDTO("Error mapping category for product."));
                }
            }

            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ProductResponseDTO("Product not found"));
        }
    }

    public ResponseEntity<ProductResponseDTO> handleProductUpdatedEvent(ProductUpdatedEvent event) {
        // Retrieve the product from the repository
        Optional<Product> productOptional = productRepository.findById(event.getProductId());

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            ProductResponseDTO responseDTO = modelMapper.map(product, ProductResponseDTO.class);

            // Check if product has a valid category before mapping
            if (product.getCategory() != null) {
                Category category = categoryRepository.findById(product.getCategory()).orElse(null);

                if (category != null) {
                    responseDTO.setCategory(modelMapper.map(category, CategoryDTO.class));
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ProductResponseDTO("Error mapping category for product."));
                }
            }

            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ProductResponseDTO("Product not found"));
        }
    }

    public ResponseEntity<Void> handleProductDeletedEvent(ProductDeletedEvent event) {
        // Handle the event as needed (e.g., logging, cleanup tasks)
        // You may not need to return a response entity for a void method

        return ResponseEntity.ok().build();
    }


}
