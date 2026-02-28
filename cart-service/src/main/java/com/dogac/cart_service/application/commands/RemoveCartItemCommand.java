package com.dogac.cart_service.application.commands;

import java.util.UUID;

import com.dogac.cart_service.application.core.Command;

public record RemoveCartItemCommand(
        UUID cartId,
        UUID productId) implements Command<Void> {
}
