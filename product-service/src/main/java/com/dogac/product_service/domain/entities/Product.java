package com.dogac.product_service.domain.entities;

import java.time.LocalDateTime;

import com.dogac.product_service.domain.valueobjects.Description;
import com.dogac.product_service.domain.valueobjects.Money;
import com.dogac.product_service.domain.valueobjects.ProductId;
import com.dogac.product_service.domain.valueobjects.ProductName;
import com.dogac.product_service.domain.valueobjects.StockQuantity;

public class Product {
    private ProductId id;
    private ProductName name;
    private Description description;
    private Money price;
    private StockQuantity stockQuantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @SuppressWarnings("unused")
    private Product() {
        // Private constructor for JPA/Hibernate or object mapping frameworks
    }

    public Product(ProductId id, ProductName name, Description description, Money price, StockQuantity stockQuantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateName(ProductName newName) {
        this.name = newName;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateDescription(Description newDescription) {
        this.description = newDescription;
        this.updatedAt = LocalDateTime.now();
    }

    public void updatePrice(Money newPrice) {
        this.price = newPrice;
        this.updatedAt = LocalDateTime.now();
    }

    public void increaseStock(StockQuantity quantity) {
        if (quantity.value() <= 0) {
            throw new IllegalArgumentException("Stock increase quantity must be positive.");
        }
        this.stockQuantity = new StockQuantity(this.stockQuantity.value() + quantity.value());
        this.updatedAt = LocalDateTime.now();
    }

    public void decreaseStock(StockQuantity quantity) {
        if (quantity.value() <= 0) {
            throw new IllegalArgumentException("Stock decrease quantity must be positive.");
        }
        if (this.stockQuantity.value() < quantity.value()) {
            throw new IllegalStateException("Insufficient stock. Available: " + this.stockQuantity.value() + ", Requested: " + quantity.value());
        }
        this.stockQuantity = new StockQuantity(this.stockQuantity.value() - quantity.value());
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isInStock() {
        return this.stockQuantity.value() > 0;
    }

    public boolean hasEnoughStock(int quantity) {
        return this.stockQuantity.value() >= quantity;
    }

    // Getters
    public ProductId getId() {
        return id;
    }

    public ProductName getName() {
        return name;
    }

    public Description getDescription() {
        return description;
    }

    public Money getPrice() {
        return price;
    }

    public StockQuantity getStockQuantity() {
        return stockQuantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
