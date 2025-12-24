package com.dogac.product_service.domain.valueobjects;

import java.util.Objects;

public record Description(String value) {

    public Description {
        Objects.requireNonNull(value, "Description cannot be null.");
        if (value.length() > 1000) {
            throw new IllegalArgumentException("Description cannot exceed 1000 characters.");
        }
    }
}
