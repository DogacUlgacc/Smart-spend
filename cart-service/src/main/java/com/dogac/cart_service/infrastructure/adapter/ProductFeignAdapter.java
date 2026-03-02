package com.dogac.cart_service.infrastructure.adapter;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.dogac.cart_service.application.dto.feignDto.Currency;
import com.dogac.cart_service.application.dto.feignDto.ProductDto;
import com.dogac.cart_service.application.port.ProductPort;
import com.dogac.cart_service.infrastructure.feignclient.ProductClient;

@Component
public class ProductFeignAdapter implements ProductPort {

    private final ProductClient productClient;

    public ProductFeignAdapter(ProductClient productClient) {
        this.productClient = productClient;
    }

    @Override
    public ProductDto getProductById(UUID id) {
        ProductDto response = productClient.getProductById(id);

        // ProductResponse.currency -> CartService Currency enum
        Currency currency = Currency.valueOf(response.currency().name());

        return new ProductDto(
                response.id(),
                response.productName(),
                response.productDescription(),
                response.amount(),
                currency,
                response.stockQuantity());
    }
}
