package com.dogac.cart_service.application.commands;

import java.math.BigDecimal;
import java.util.UUID;

import com.dogac.cart_service.application.core.Command;
import com.dogac.cart_service.domain.enums.CurrencyType;

public record AddItemToCartCommand(
                UUID userId,
                UUID productId,
                Integer quantity,
                BigDecimal price,
                CurrencyType currency) implements Command<Void> {

}
