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

    public static Quantity of(Integer quantity) {
        if (quantity == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
        }
        return new Quantity(quantity);
    }

    public static Integer toInteger(Quantity quantity) {
        if (quantity == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
        }
        return new Integer(quantity.value);
    }

}
