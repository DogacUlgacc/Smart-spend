package com.dogac.cart_service.application.commands;

import java.util.UUID;

import com.dogac.cart_service.application.core.Command;

public record AddItemToCartCommand(
                UUID productId,
                Integer quantity) implements Command<Void> {

}
