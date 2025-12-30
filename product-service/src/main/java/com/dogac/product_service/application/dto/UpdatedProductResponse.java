package com.dogac.product_service.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.dogac.product_service.domain.enums.Currency;

public record UpdatedProductResponse(
                UUID id,
                String productName,
                String productDescription,
                BigDecimal amount,
                Currency currency,
                Integer stockQuantity) {
}
