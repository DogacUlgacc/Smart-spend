package com.dogac.product_service.application.commands;

import java.math.BigDecimal;
import java.util.UUID;

import com.dogac.product_service.application.core.Command;
import com.dogac.product_service.application.dto.UpdatedProductResponse;
import com.dogac.product_service.domain.enums.Currency;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateProductCommand(
        @NotBlank UUID id,
        @NotBlank @Size(max = 255) String productName,
        @NotBlank String productDescription,
        @NotNull @DecimalMin("0.0") BigDecimal amount,
        @NotNull Currency currency,
        @NotNull @Min(0) Integer stockQuantity) implements Command<UpdatedProductResponse> {
}
