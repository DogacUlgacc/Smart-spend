package com.dogac.common_events.event;

import java.math.BigDecimal;
import java.util.UUID;

import com.dogac.common_events.enums.Currency;

public record CartItemAddedEvent(
        UUID cartId,
        UUID userId,
        UUID productId,
        Integer quantity,
        BigDecimal price,
        Currency currency) {
}

