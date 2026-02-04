package com.dogac.cart_service.application.commands;

import java.math.BigDecimal;
import java.util.UUID;

import com.dogac.cart_service.application.core.Command;

public record AddItemToCartCommand(
        UUID userId,
        UUID productId,
        Integer quantity,
        BigDecimal price,
        String currency) implements Command<Void> {

}
