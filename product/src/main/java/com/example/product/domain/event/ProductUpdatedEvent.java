package com.example.product.domain.event;

public class ProductUpdatedEvent {
    private final int productId;

    public ProductUpdatedEvent(int productId) {
        this.productId = productId;
    }

    public int getProductId() {
        return productId;
    }
}
