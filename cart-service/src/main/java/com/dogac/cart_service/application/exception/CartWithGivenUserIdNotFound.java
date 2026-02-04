package com.dogac.cart_service.application.exception;

public class CartWithGivenUserIdNotFound extends RuntimeException {

    public CartWithGivenUserIdNotFound(String messageString) {
        super(messageString);
    }
}
