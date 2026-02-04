package com.dogac.cart_service.domain.repositories;

import java.util.Optional;

import com.dogac.cart_service.domain.cart.Cart;
import com.dogac.cart_service.domain.valueobjects.UserId;

public interface CartRepository {

    Optional<Cart> findByUserId(UserId userId);

    void save(Cart cart);

}
