package com.dogac.cart_service.infrastructure;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.dogac.common_events.event.CartItemAddedEvent;

@Component
public class KafkaEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishCartItemAdded(CartItemAddedEvent event) {
        kafkaTemplate.send("cart-item-added", event);
    }
}

