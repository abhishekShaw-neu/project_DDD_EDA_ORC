package com.example.product.controller;

import com.example.product.application.dto.ProductRequestDTO;
import com.example.product.application.dto.ProductResponseDTO;
import com.example.product.domain.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ProductResponseDTO> findProductById(@PathVariable("productId") int id) {
        // Delegate to the orchestration service to handle the retrieval of a product by ID
        return productService.getProductById(id);
    }

    @PostMapping("/product")
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        // Delegate to the service to handle the creation of a product
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProductAndHandleEvent(productRequestDTO).getBody());
    }

    @PutMapping("/product/{productId}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable("productId") int productId, @RequestBody ProductRequestDTO productRequestDTO) {
        // Delegate to the service to handle the update of a product
        return productService.updateProductAndHandleEvent(productId, productRequestDTO);
    }

    @DeleteMapping("/product/{productId}")
    public ResponseEntity<Void> removeProduct(@PathVariable("productId") int productId) {
        // Delegate to the service to handle the deletion of a product
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
