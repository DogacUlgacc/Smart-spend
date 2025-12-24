package com.dogac.product_service.domain.exceptions;

public class InvalidPriceChangeException extends RuntimeException {

    public InvalidPriceChangeException(String message) {
        super(message);
    }
}