package com.dogac.cart_service.domain.valueobjects;

import java.util.Objects;

public record Quantity(Integer value) {

    public Quantity {
        Objects.requireNonNull(value, "Quantity cannot be null");
        if (value <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
    }

    public Quantity add(Quantity other) {
        return new Quantity(this.value + other.value);
    }

}
