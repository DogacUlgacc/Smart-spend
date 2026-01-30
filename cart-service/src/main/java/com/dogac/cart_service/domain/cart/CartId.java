package com.dogac.cart_service.domain.cart;

import java.util.Objects;
import java.util.UUID;

public record CartId(UUID value) {

    public CartId {
        Objects.requireNonNull(value, "Value for CartId cannot be null!");
    }

    public static CartId generate() {
        return new CartId(UUID.randomUUID());
    }

    public static CartId from(UUID value) {
        return new CartId(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
