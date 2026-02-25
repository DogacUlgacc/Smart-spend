package com.dogac.cart_service.domain.repositories;

import java.util.Optional;

import com.dogac.cart_service.domain.cart.Cart;
import com.dogac.cart_service.domain.cart.CartId;
import com.dogac.cart_service.domain.valueobjects.UserId;

public interface CartRepository {

    Optional<Cart> findByUserId(UserId userId);

    Optional<Cart> findById(CartId cartId);

    Optional<Cart> findByIdAndUserId(CartId cartId, UserId userId);

    void save(Cart cart);

}
