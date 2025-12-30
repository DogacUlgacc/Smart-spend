package com.dogac.product_service.application.dto;

import java.math.BigDecimal;

import com.dogac.product_service.domain.enums.Currency;

public record UpdateProductRequest(
        String productName,
        String productDescription,
        BigDecimal amount,
        Currency currency,
        Integer stockQuantity) {
}
