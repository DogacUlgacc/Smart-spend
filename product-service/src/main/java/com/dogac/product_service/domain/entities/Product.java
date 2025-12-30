package com.dogac.product_service.domain.entities;

import java.util.Objects;

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

    }

    public static Product create(ProductName productName, Description description, Money money,
            StockQuantity stockQuantity) {

        return new Product(ProductId.generate(),
                Objects.requireNonNull(productName),
                Objects.requireNonNull(description),
                Objects.requireNonNull(money),
                Objects.requireNonNull(stockQuantity));
    }

    public static Product rehydrate(ProductId productId, ProductName productName, Description description, Money money,
            StockQuantity stockQuantity) {
        return new Product(productId, productName, description, money, stockQuantity);
    }

    public void updateName(ProductName newName) {
        this.name = newName;

    }

    public void updateStockQuantity(StockQuantity newStockQuantity) {
        this.stockQuantity = newStockQuantity;
    }

    public void updateDescription(Description newDescription) {
        this.description = newDescription;

    }

    public void updatePrice(Money newPrice) {
        this.price = newPrice;

    }

    public void increaseStock(StockQuantity quantity) {
        if (quantity.value() <= 0) {
            throw new IllegalArgumentException("Stock increase quantity must be positive.");
        }
        this.stockQuantity = new StockQuantity(this.stockQuantity.value() + quantity.value());

    }

    public void decreaseStock(StockQuantity quantity) {
        if (quantity.value() <= 0) {
            throw new IllegalArgumentException("Stock decrease quantity must be positive.");
        }
        if (this.stockQuantity.value() < quantity.value()) {
            throw new IllegalStateException("Insufficient stock. Available: " + this.stockQuantity.value()
                    + ", Requested: " + quantity.value());
        }
        this.stockQuantity = new StockQuantity(this.stockQuantity.value() - quantity.value());
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

}
