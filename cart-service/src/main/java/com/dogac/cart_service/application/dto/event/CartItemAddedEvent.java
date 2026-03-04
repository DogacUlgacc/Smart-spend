package com.dogac.cart_service.application.dto.event;

import java.math.BigDecimal;
import java.util.UUID;

import com.dogac.cart_service.domain.enums.Currency;

public record CartItemAddedEvent(
                UUID cartId,
                UUID userId,
                UUID productId,
                Integer quantity,
                BigDecimal price,
                Currency currency) {

}
