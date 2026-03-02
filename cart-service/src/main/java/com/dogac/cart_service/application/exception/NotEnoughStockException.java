package com.dogac.cart_service.application.exception;

public class NotEnoughStockException extends RuntimeException {
    public NotEnoughStockException(String msg) {
        super(msg);
    }

}
