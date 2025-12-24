package com.dogac.product_service.domain.services;

import com.dogac.product_service.domain.entities.Product;
import com.dogac.product_service.domain.exceptions.DuplicateProductException;
import com.dogac.product_service.domain.exceptions.InsufficientStockException;
import com.dogac.product_service.domain.exceptions.InvalidPriceChangeException;
import com.dogac.product_service.domain.exceptions.ProductNotFoundException;
import com.dogac.product_service.domain.repositories.ProductRepository;
import com.dogac.product_service.domain.valueobjects.Money;
import com.dogac.product_service.domain.valueobjects.ProductId;
import com.dogac.product_service.domain.valueobjects.StockQuantity;

public class ProductDomainService {
    private final ProductRepository productRepository;

    public ProductDomainService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validateProductCreation(Product product) {
        if (productRepository.existsByName(product.getName().value())) {
            throw new DuplicateProductException(
                    "Product with name '" + product.getName().value() + "' already exists.");
        }
    }

    public void reserveStock(ProductId productId, StockQuantity quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId.value()));

        if (!product.hasEnoughStock(quantity.value())) {
            throw new InsufficientStockException(
                    "Insufficient stock for product: " + product.getName().value() +
                            ". Available: " + product.getStockQuantity().value() +
                            ", Requested: " + quantity.value());
        }

        product.decreaseStock(quantity);
        productRepository.save(product);
    }

    public void releaseStock(ProductId productId, StockQuantity quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId.value()));

        product.increaseStock(quantity);
        productRepository.save(product);
    }

    public void validatePriceChange(ProductId productId, Money newPrice) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId.value()));

        if (newPrice.amount().intValue() <= 0) {
            throw new InvalidPriceChangeException("New Price cannot be 0");
        }
    }
}
