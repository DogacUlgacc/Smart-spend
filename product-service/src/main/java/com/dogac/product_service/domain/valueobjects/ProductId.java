package com.dogac.product_service.domain.valueobjects;

import java.util.Objects;
import java.util.UUID;

public record ProductId(UUID value) {

    public ProductId {
        Objects.requireNonNull(value, "Value for ProductId cannot be null.");
    }

    public static ProductId generate() {
        return new ProductId(UUID.randomUUID());
    }
}
