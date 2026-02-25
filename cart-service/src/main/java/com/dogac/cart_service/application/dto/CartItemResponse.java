package com.dogac.cart_service.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CartItemResponse(
                UUID productId,
                Integer quantity,
                BigDecimal unitPrice,
                BigDecimal totalPrice) {
}