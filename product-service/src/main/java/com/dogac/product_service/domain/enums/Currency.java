package com.dogac.product_service.domain.enums;

public enum Currency {

    TRY("₺"),
    USD("$"),
    EUR("€");

    private final String symbol;

    Currency(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}