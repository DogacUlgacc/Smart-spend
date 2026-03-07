package com.dogac.product_service.application.dto.event;

import java.util.UUID;

public record CartItemRemovedEvent(
        UUID cartId,
        UUID productId,
        Integer quantity) {
}