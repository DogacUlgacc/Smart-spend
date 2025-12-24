package com.dogac.product_service.domain.valueobjects;

import java.util.Objects;

public record StockQuantity(Integer value) {
    public StockQuantity {
        Objects.requireNonNull(value, "StockQuantity cannot be null");
        if (value < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative.");
        }
    }

}
