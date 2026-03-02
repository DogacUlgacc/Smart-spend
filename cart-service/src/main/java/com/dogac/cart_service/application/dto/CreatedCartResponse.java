package com.dogac.cart_service.application.dto;

import java.util.UUID;

import com.dogac.cart_service.domain.enums.Currency;

public record CreatedCartResponse(
        UUID cartId,
        UUID userId,
        Currency currency) {

}
