package com.dogac.cart_service.infrastructure.adapter;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.dogac.cart_service.domain.cart.Cart;
import com.dogac.cart_service.domain.cart.CartId;
import com.dogac.cart_service.domain.repositories.CartRepository;
import com.dogac.cart_service.domain.valueobjects.UserId;
import com.dogac.cart_service.infrastructure.mapper.CartMapper;
import com.dogac.cart_service.infrastructure.repositories.SpringDataCartRepository;

@Repository
public class CartRepositoryAdapter implements CartRepository {

    private final SpringDataCartRepository cartRepository;
    private final CartMapper cartMapper;

    public CartRepositoryAdapter(SpringDataCartRepository cartRepository, CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
    }

    @Override
    public Optional<Cart> findByUserId(UserId userId) {
        return cartRepository.findByUserId(userId.value())
                .map(cartMapper::toDomain);
    }

    @Override
    public void save(Cart cart) {
        cartRepository.save(cartMapper.toEntity(cart));
    }

    @Override
    public Optional<Cart> findById(CartId cartId) {
        return cartRepository.findById(cartId.value()).map(cartMapper::toDomain);
    }

    @Override
    public Optional<Cart> findByIdAndUserId(CartId cartId, UserId userId) {
        return cartRepository.findByIdAndUserId(cartId.value(), userId.value()).map(cartMapper::toDomain);
    }

}
