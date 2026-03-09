package com.dogac.common_events.event;

import java.util.UUID;

public record CartItemRemovedEvent(
        UUID cartId,
        UUID productId,
        Integer quantity) {
}

