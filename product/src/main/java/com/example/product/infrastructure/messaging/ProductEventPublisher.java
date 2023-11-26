package com.example.product.infrastructure.messaging;

import com.example.product.domain.event.ProductCreatedEvent;
import com.example.product.domain.event.ProductDeletedEvent;
import com.example.product.domain.event.ProductUpdatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class ProductEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public ProductEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publishProductCreatedEvent(ProductCreatedEvent event) {
        eventPublisher.publishEvent(event);
    }

    public void publishProductUpdatedEvent(ProductUpdatedEvent event) {
        eventPublisher.publishEvent(event);
    }

    public void publishProductDeletedEvent(ProductDeletedEvent event) {
        eventPublisher.publishEvent(event);
    }
}
