package com.dogac.product_service.application.mapper;

import org.springframework.stereotype.Component;

import com.dogac.product_service.application.dto.ProductResponse;
import com.dogac.product_service.domain.entities.Product;
import com.dogac.product_service.infrastructure.entities.JpaProductEntity;

@Component
public class GetProductMapper {

    public ProductResponse toDomain(JpaProductEntity entity) {
        ProductResponse response = new ProductResponse(
                entity.getId(),
                entity.getProductName(),
                entity.getProductDescription(),
                entity.getPrice().amount(),
                entity.getPrice().currency(),
                entity.getStockQuantity());
        return response;

    }

    public ProductResponse toResponse(Product product) {

        ProductResponse response = new ProductResponse(
                product.getId().value(),
                product.getName().value(),
                product.getDescription().value(),
                product.getPrice().amount(),
                product.getPrice().currency(),
                product.getStockQuantity().value());

        return response;
    }
}
