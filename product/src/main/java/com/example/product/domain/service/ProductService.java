package com.example.product.domain.service;


import com.example.product.application.dto.ProductRequestDTO;
import com.example.product.application.dto.ProductResponseDTO;
import com.example.product.domain.event.ProductCreatedEvent;
import com.example.product.domain.event.ProductUpdatedEvent;
import com.example.product.domain.service.command.ProductCommandService;
import com.example.product.domain.service.event.ProductEventService;
import com.example.product.domain.service.query.ProductQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductCommandService productCommandService;
    private final ProductEventService productEventService;

    private final ProductQueryService productQueryService;

    @Autowired
    public ProductService(ProductCommandService productCommandService, ProductEventService productEventService,ProductQueryService productQueryService) {
        this.productCommandService = productCommandService;
        this.productEventService = productEventService;
        this.productQueryService=productQueryService;
    }

    public ResponseEntity<ProductResponseDTO> createProductAndHandleEvent(ProductRequestDTO productRequestDTO) {
        // Create product
        ResponseEntity<ProductResponseDTO> responseEntity = productCommandService.createProduct(productRequestDTO);

        // Check if the request was successful (status code 2xx)
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            ProductResponseDTO productResponseDTO = responseEntity.getBody();

            // Manually handle ProductCreatedEvent
            ProductCreatedEvent createdEvent = new ProductCreatedEvent(productResponseDTO.getId());
            productEventService.handleProductCreatedEvent(createdEvent);

            return ResponseEntity.status(HttpStatus.CREATED).body(productResponseDTO);
        } else {
            // Handle the case where the request was not successful
            // You might want to throw an exception, log an error, or handle it based on your requirements
            throw new RuntimeException("Failed to create product. HTTP status code: " + responseEntity.getStatusCodeValue());
        }
    }

    public ResponseEntity<ProductResponseDTO> updateProductAndHandleEvent(int productId, ProductRequestDTO productRequestDTO) {
        // Update product
        ResponseEntity<ProductResponseDTO> responseEntity = productCommandService.updateProduct(productId, productRequestDTO);

        // Check if the request was successful (status code 2xx)
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            ProductResponseDTO updatedProductResponseDTO = responseEntity.getBody();

            // Manually handle ProductUpdatedEvent
            ProductUpdatedEvent updatedEvent = new ProductUpdatedEvent(productId);
            productEventService.handleProductUpdatedEvent(updatedEvent);

            return ResponseEntity.ok(updatedProductResponseDTO);
        } else {
            // Handle the case where the request was not successful
            // You might want to throw an exception, log an error, or handle it based on your requirements
            throw new RuntimeException("Failed to update product. HTTP status code: " + responseEntity.getStatusCodeValue());
        }
    }

    public ResponseEntity<ProductResponseDTO> getProductById(int productId) {
        // Implement logic to retrieve product by ID from the database
        // This is a placeholder, and you should replace it with your actual implementation
        ProductResponseDTO productResponseDTO = productQueryService.getProductById(productId).getBody();

        if (productResponseDTO != null) {
            return ResponseEntity.ok(productResponseDTO);
        } else {
            // Handle the case where the product is not found
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<String> deleteProduct(int productId) {
        // Delete product by ID using the command service
        ResponseEntity<String> responseEntity = productCommandService.deleteProduct(productId);

        // Check if the request was successful (status code 2xx)
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            // Add any additional logic if needed
            return ResponseEntity.noContent().build();
        } else {
            // Handle the case where the request was not successful
            // You might want to throw an exception, log an error, or handle it based on your requirements
            throw new RuntimeException("Failed to delete product. HTTP status code: " + responseEntity.getStatusCodeValue());
        }
    }

    // Additional orchestrated operations can be added here
}
