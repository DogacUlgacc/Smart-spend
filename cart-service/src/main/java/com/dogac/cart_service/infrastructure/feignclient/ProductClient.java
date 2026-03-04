package com.dogac.cart_service.infrastructure.feignclient;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.dogac.cart_service.application.dto.feignDto.ProductDto;

@FeignClient(name = "product-service", url = "http://localhost:8888/api/v1/products")
public interface ProductClient {

    @GetMapping("/{id}")
    ProductDto getProductById(@PathVariable("id") UUID id);
}
