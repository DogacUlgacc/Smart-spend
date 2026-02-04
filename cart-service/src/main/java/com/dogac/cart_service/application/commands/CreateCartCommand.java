package com.dogac.cart_service.application.commands;

import com.dogac.cart_service.application.core.Command;
import com.dogac.cart_service.application.dto.CreatedCartResponse;

import jakarta.validation.constraints.NotBlank;

public record CreateCartCommand(
                @NotBlank String userId,
                @NotBlank String currency) implements Command<CreatedCartResponse> {
}
