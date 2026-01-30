package com.dogac.cart_service.domain.services;

import com.dogac.cart_service.domain.cart.Cart;
import com.dogac.cart_service.domain.cart.CartItem;
import com.dogac.cart_service.domain.repositories.CartRepository;
import com.dogac.cart_service.domain.valueobjects.Money;
import com.dogac.cart_service.domain.valueobjects.ProductId;

public class CartDomainService {

    private final CartRepository cartRepository;

    public CartDomainService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public void addItem(Cart cart, CartItem item) {
        if (cart.isEmpty()) {
            cart.addItem(item.getProductId(), item.getQuantity(), item.getPrice());
        } else {
            cart.changeItemQuantity(item.getProductId(), item.getQuantity());
        }
    }

    public void removeItem(Cart cart, ProductId productId) {
        if (!cart.containsProduct(productId)) {
            throw new IllegalArgumentException("There are no such products in the specified cart.");
        }
        cart.removeItem(productId);
    }

    public Money calculateTotalPrice(Cart cart) {
        return cart.totalAmount();
    }

}
