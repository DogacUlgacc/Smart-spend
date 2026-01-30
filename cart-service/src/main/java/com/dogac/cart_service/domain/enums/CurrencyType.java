package com.dogac.cart_service.domain.enums;

public enum CurrencyType {

    TRY("₺"),
    USD("$"),
    EUR("€");

    private final String symbol;

    CurrencyType(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}