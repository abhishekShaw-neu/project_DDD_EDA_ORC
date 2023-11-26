package com.example.product.domain.event;

public class ProductDeletedEvent {
    private final int productId;

    public ProductDeletedEvent(int productId) {
        this.productId = productId;
    }

    public int getProductId() {
        return productId;
    }
}
