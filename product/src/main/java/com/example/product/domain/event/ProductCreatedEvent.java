package com.example.product.domain.event;

public class ProductCreatedEvent {
    private final int productId;

    public ProductCreatedEvent(int productId) {
        this.productId = productId;
    }

    public int getProductId() {
        return productId;
    }
}
