package com.dogac.cart_service.domain.services;

import com.dogac.cart_service.domain.cart.Cart;
import com.dogac.cart_service.domain.repositories.CartRepository;
import com.dogac.cart_service.domain.valueobjects.Money;

public class CartDomainService {
    private final CartRepository cartRepository;

    public CartDomainService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Money calculateTotalPrice(Cart cart) {
        return cart.totalAmount();
    }

}
