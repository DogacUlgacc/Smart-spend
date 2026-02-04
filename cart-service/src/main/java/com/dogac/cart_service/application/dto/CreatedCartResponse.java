package com.dogac.cart_service.application.dto;

import java.util.UUID;

public record CreatedCartResponse(
        UUID cartId,
        UUID userId,
        String currency) {

}
