package com.dogac.product_service.application.mapper;

import org.springframework.stereotype.Component;

import com.dogac.product_service.application.dto.UpdatedProductResponse;
import com.dogac.product_service.domain.entities.Product;

@Component
public class UpdateProductMapper {

    public UpdatedProductResponse toResponse(Product domain) {

        UpdatedProductResponse response = new UpdatedProductResponse(
                domain.getId().value(),
                domain.getName().value(),
                domain.getDescription().value(),
                domain.getPrice().amount(),
                domain.getPrice().currency(),
                domain.getStockQuantity().value());
        return response;
    }
}
