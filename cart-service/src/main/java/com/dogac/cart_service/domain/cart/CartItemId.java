package com.dogac.cart_service.domain.cart;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public record CartItemId(UUID value) implements Serializable {

    public CartItemId {
        Objects.requireNonNull(value, "Value for CartItemId cannot be null!");
    }

    public static CartItemId generate() {
        return new CartItemId(UUID.randomUUID());
    }

    public static CartItemId from(UUID value) {
        return new CartItemId(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
