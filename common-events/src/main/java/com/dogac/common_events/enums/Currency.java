package com.dogac.common_events.enums;

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