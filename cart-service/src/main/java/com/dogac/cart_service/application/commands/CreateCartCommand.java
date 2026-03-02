package com.dogac.cart_service.application.commands;

import java.util.UUID;

import com.dogac.cart_service.application.core.Command;
import com.dogac.cart_service.application.dto.CreatedCartResponse;
import com.dogac.cart_service.domain.enums.Currency;

import jakarta.validation.constraints.NotBlank;

public record CreateCartCommand(
        @NotBlank UUID userId,
        @NotBlank Currency currency) implements Command<CreatedCartResponse> {
}
