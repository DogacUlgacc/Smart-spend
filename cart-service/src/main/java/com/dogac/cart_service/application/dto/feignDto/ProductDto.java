package com.dogac.cart_service.application.dto.feignDto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductDto(UUID id,
        String productName,
        String productDescription,
        BigDecimal amount,
        Currency currency,
        Integer stockQuantity) {

}
