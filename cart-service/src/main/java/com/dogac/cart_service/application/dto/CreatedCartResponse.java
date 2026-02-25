package com.dogac.cart_service.application.dto;

import java.util.UUID;

import com.dogac.cart_service.domain.enums.CurrencyType;

public record CreatedCartResponse(
                UUID cartId,
                UUID userId,
                CurrencyType currency) {

}
