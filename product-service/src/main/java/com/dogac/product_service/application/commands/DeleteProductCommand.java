package com.dogac.product_service.application.commands;

import java.util.UUID;

import com.dogac.product_service.application.core.Command;

import jakarta.validation.constraints.NotBlank;

public record DeleteProductCommand(@NotBlank UUID id) implements Command<Void> {
}
