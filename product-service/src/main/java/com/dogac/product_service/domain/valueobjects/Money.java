package com.dogac.product_service.domain.valueobjects;

import java.math.BigDecimal;
import java.util.Objects;

import com.dogac.product_service.domain.enums.Currency;

public record Money(BigDecimal amount, Currency currency) {

    public Money {
        Objects.requireNonNull(amount, "Amount cannot be null.");
        Objects.requireNonNull(currency, "Currency cannot be null.");
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative.");
        }
    }
}
