package com.dogac.cart_service.domain.valueobjects;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import com.dogac.cart_service.domain.enums.CurrencyType;

public record Money(BigDecimal amount, CurrencyType currency) {

    public Money {
        Objects.requireNonNull(amount, "Amount cannot be null");
        Objects.requireNonNull(currency, "Currency cannot be null");

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Money amount cannot be negative");
        }

        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    public static Money zero(CurrencyType currency) {
        return new Money(BigDecimal.ZERO, currency);
    }

    public Money add(Money other) {
        validateSameCurrency(other);
        return new Money(this.amount.add(other.amount), currency);
    }

    public Money multiply(Quantity quantity) {
        return new Money(
                this.amount.multiply(BigDecimal.valueOf(quantity.value())),
                currency);
    }

    private void validateSameCurrency(Money other) {
        if (this.currency != other.currency) {
            throw new IllegalArgumentException("Currencies must be the same");
        }
    }
}
