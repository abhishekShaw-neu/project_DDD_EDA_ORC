package com.example.product.domain.service.command;

import com.example.product.application.dto.CategoryDTO;
import com.example.product.application.dto.ProductRequestDTO;
import com.example.product.application.dto.ProductResponseDTO;
import com.example.product.domain.event.ProductCreatedEvent;
import com.example.product.domain.event.ProductDeletedEvent;
import com.example.product.domain.event.ProductUpdatedEvent;
import com.example.product.domain.model.Category;
import com.example.product.domain.model.Product;
import com.example.product.domain.repository.CategoryRepository;
import com.example.product.domain.repository.ProductRepository;
import com.example.product.infrastructure.messaging.ProductEventPublisher;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductCommandService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductEventPublisher eventPublisher;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductCommandService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            ProductEventPublisher eventPublisher,
            ModelMapper modelMapper
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.eventPublisher = eventPublisher;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<ProductResponseDTO> createProduct(ProductRequestDTO productRequestDTO) {
        try {
            Category category = categoryRepository.findById(productRequestDTO.getCategory()).orElse(null);

            if (category != null) {
                Product product = modelMapper.map(productRequestDTO, Product.class);
                product.setCategory(category.getCategoryId()); // Assuming product.getCategory() returns the category ID
                Product savedProduct = productRepository.save(product);

                ProductResponseDTO productResponseDTO = modelMapper.map(savedProduct, ProductResponseDTO.class);
                productResponseDTO.setCategory(modelMapper.map(category, CategoryDTO.class));

                // Publish ProductCreatedEvent
                ProductCreatedEvent createdEvent = new ProductCreatedEvent(savedProduct.getProductId());
                eventPublisher.publishProductCreatedEvent(createdEvent);

                return ResponseEntity.status(HttpStatus.CREATED).body(productResponseDTO);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ProductResponseDTO("Category not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ProductResponseDTO("Error creating product: " + e.getMessage()));
        }
    }


    public ResponseEntity<ProductResponseDTO> updateProduct(Integer productId, ProductRequestDTO updatedProductRequest) {
        try {
            Optional<Product> existingProductOptional = productRepository.findById(productId);

            if (existingProductOptional.isPresent()) {
                Product existingProduct = existingProductOptional.get();

                modelMapper.map(updatedProductRequest, existingProduct);

                Category category = categoryRepository.findById(updatedProductRequest.getCategory()).orElse(null);

                if (category != null) {
                    existingProduct.setCategory(category.getCategoryId());

                    Product updatedProduct = productRepository.save(existingProduct);

                    ProductResponseDTO responseDTO = modelMapper.map(updatedProduct, ProductResponseDTO.class);
                    responseDTO.setCategory(modelMapper.map(category, CategoryDTO.class));

                    // Publish ProductUpdatedEvent
                    ProductUpdatedEvent updatedEvent = new ProductUpdatedEvent(updatedProduct.getProductId());
                    eventPublisher.publishProductUpdatedEvent(updatedEvent);

                    return ResponseEntity.ok(responseDTO);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ProductResponseDTO("Category not found"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ProductResponseDTO("Product not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ProductResponseDTO("Error updating product: " + e.getMessage()));
        }
    }

    public ResponseEntity<String> deleteProduct(int productId) {
        try {
            Optional<Product> productOptional = productRepository.findById(productId);

            if (productOptional.isPresent()) {
                productRepository.deleteById(productId);

                // Publish ProductDeletedEvent
                ProductDeletedEvent deletedEvent = new ProductDeletedEvent(productId);
                eventPublisher.publishProductDeletedEvent(deletedEvent);

                return ResponseEntity.ok("Product deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating product: " + e.getMessage());
        }
    }

}

