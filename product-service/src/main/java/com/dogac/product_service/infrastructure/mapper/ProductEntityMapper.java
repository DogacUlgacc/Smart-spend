package com.dogac.product_service.infrastructure.mapper;

import org.springframework.stereotype.Component;

import com.dogac.product_service.domain.entities.Product;
import com.dogac.product_service.domain.valueobjects.Description;
import com.dogac.product_service.domain.valueobjects.Money;
import com.dogac.product_service.domain.valueobjects.ProductId;
import com.dogac.product_service.domain.valueobjects.ProductName;
import com.dogac.product_service.domain.valueobjects.StockQuantity;
import com.dogac.product_service.infrastructure.entities.JpaProductEntity;

@Component
public class ProductEntityMapper {

    public Product toDomain(JpaProductEntity entity) {
        return Product.rehydrate(new ProductId(entity.getId()),
                new ProductName(entity.getProductName()),
                new Description(entity.getProductDescription()),
                new Money(entity.getPrice().amount(), entity.getPrice().currency()),
                new StockQuantity((entity.getStockQuantity())));
    }

    public JpaProductEntity toEntity(Product domain) {
        JpaProductEntity entity = new JpaProductEntity();
        entity.setId(domain.getId().value());
        entity.setProductDescription(domain.getDescription().value());
        entity.setProductName(domain.getName().value());
        entity.setStockQuantity(domain.getStockQuantity().value());
        entity.setPrice(new Money(domain.getPrice().amount(), domain.getPrice().currency()));
        return entity;

    }
}
