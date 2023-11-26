package com.example.product.domain.service.query;

import com.example.product.application.dto.CategoryDTO;
import com.example.product.application.dto.ProductResponseDTO;
import com.example.product.domain.event.ProductCreatedEvent;
import com.example.product.domain.event.ProductUpdatedEvent;
import com.example.product.domain.model.Category;
import com.example.product.domain.model.Product;
import com.example.product.domain.repository.CategoryRepository;
import com.example.product.domain.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductQueryService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductQueryService(ProductRepository productRepository, CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<ProductResponseDTO> getProductById(int productId) {
        Optional<Product> productOptional = productRepository.findById(productId);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            ProductResponseDTO responseDTO = mapProductToResponseDTO(product);
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<Product> allProducts = productRepository.findAll();

        if (!allProducts.isEmpty()) {
            List<ProductResponseDTO> responseDTOs = allProducts.stream()
                    .map(this::mapProductToResponseDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(responseDTOs);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Event listeners to handle events
    @EventListener
    @Transactional
    public void handleProductCreatedEvent(ProductCreatedEvent event) {
        // Handle the event (e.g., update projections, materialized views, etc.)
    }

    @EventListener
    @Transactional
    public void handleProductUpdatedEvent(ProductUpdatedEvent event) {
        // Handle the event (e.g., update projections, materialized views, etc.)
    }

    private ProductResponseDTO mapProductToResponseDTO(Product product) {
        ProductResponseDTO responseDTO = modelMapper.map(product, ProductResponseDTO.class);

        if (product.getCategory() != null) {
            Category category = categoryRepository.findById(product.getCategory()).orElse(null);

            if (category != null) {
                responseDTO.setCategory(modelMapper.map(category, CategoryDTO.class));
            }
        }

        return responseDTO;
    }
}
