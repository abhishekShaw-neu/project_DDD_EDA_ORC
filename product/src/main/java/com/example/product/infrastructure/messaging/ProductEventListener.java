package com.example.product.infrastructure.messaging;

import com.example.product.domain.event.ProductCreatedEvent;
import com.example.product.domain.event.ProductDeletedEvent;
import com.example.product.domain.event.ProductUpdatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ProductEventListener {

    @EventListener
    public void handleProductCreatedEvent(ProductCreatedEvent event) {
        // Handle the ProductCreatedEvent
        System.out.println("Product Created Event Received: id =  " + event.getProductId());
        // Additional logic can be added here
    }

    @EventListener
    public void handleProductUpdatedEvent(ProductUpdatedEvent event) {
        // Handle the ProductUpdatedEvent
        System.out.println("Product Updated Event Received: id = " + event.getProductId());
        // Additional logic can be added here
    }

    @EventListener
    public void handleProductDeletedEvent(ProductDeletedEvent event) {
        // Handle the ProductDeletedEvent
        System.out.println("Product Deleted Event Received: id = " + event.getProductId());
        // Additional logic can be added here
    }
}
