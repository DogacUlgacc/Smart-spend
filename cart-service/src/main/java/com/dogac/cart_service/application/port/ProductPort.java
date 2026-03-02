package com.dogac.cart_service.application.port;

import java.util.UUID;

import com.dogac.cart_service.application.dto.feignDto.ProductDto;

public interface ProductPort {
    ProductDto getProductById(UUID id);
}
