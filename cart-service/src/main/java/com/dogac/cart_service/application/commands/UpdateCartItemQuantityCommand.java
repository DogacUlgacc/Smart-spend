package com.dogac.cart_service.application.commands;

import java.util.UUID;

import com.dogac.cart_service.application.core.Command;
import com.dogac.cart_service.application.dto.CartResponse;

public record UpdateCartItemQuantityCommand(
                UUID cartId,
                UUID userId,
                UUID productId,
                Integer quantity) implements Command<CartResponse> {
}
