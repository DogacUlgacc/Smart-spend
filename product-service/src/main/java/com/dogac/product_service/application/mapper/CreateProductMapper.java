package com.dogac.product_service.application.mapper;

import org.springframework.stereotype.Component;

import com.dogac.product_service.application.commands.CreateProductCommand;
import com.dogac.product_service.application.dto.CreatedProductResponse;
import com.dogac.product_service.domain.entities.Product;
import com.dogac.product_service.domain.valueobjects.Description;
import com.dogac.product_service.domain.valueobjects.Money;
import com.dogac.product_service.domain.valueobjects.ProductName;
import com.dogac.product_service.domain.valueobjects.StockQuantity;

@Component
public class CreateProductMapper {

    public Product toDomain(CreateProductCommand command) {
        return Product.create(
                new ProductName(command.productName()),
                new Description(command.productDescription()),
                new Money(command.amount(), command.currency()),
                new StockQuantity(command.stockQuantity()));
    }

    public CreatedProductResponse toResponse(Product product) {
        return new CreatedProductResponse(
                product.getId().value(),
                product.getName().value(),
                product.getDescription().value(),
                product.getPrice().amount(),
                product.getPrice().currency(),
                product.getStockQuantity().value());
    }
}
