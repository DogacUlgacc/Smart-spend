package com.dogac.product_service.application.dto.event;

import java.math.BigDecimal;
import java.util.UUID;

import com.dogac.product_service.domain.enums.Currency;

public record CartItemAddedEvent(
                UUID cartId,
                UUID userId,
                UUID productId,
                Integer quantity,
                BigDecimal price,
                Currency currency) {

}
