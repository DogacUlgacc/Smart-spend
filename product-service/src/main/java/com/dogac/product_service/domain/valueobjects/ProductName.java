package com.dogac.product_service.domain.valueobjects;

import java.util.Objects;

public record ProductName(String value) {

    public ProductName {
        Objects.requireNonNull(value, "Name field cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be blank.");
        }
        if (value.length() > 200) {
            throw new IllegalArgumentException("Product name cannot exceed 200 characters.");
        }
    }
}
