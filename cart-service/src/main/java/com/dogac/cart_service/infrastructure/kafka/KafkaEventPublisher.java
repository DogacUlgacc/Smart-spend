package com.dogac.cart_service.infrastructure.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.dogac.common_events.event.CartItemAddedEvent;
import com.dogac.common_events.event.CartItemRemovedEvent;

@Component
public class KafkaEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishCartItemAdded(CartItemAddedEvent event) {
        kafkaTemplate.send("cart-item-added", event);
    }

    public void publishRemoveCartItem(CartItemRemovedEvent event) {
        kafkaTemplate.send("cart-item-removed", event);
    }
}
