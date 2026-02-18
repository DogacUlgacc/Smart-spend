package com.dogac.cart_service.application.queryHandlers;

import org.springframework.stereotype.Component;

import com.dogac.cart_service.application.core.QueryHandler;
import com.dogac.cart_service.application.dto.CartResponse;
import com.dogac.cart_service.application.mapper.CartResponseMapper;
import com.dogac.cart_service.application.queries.GetCartByIdQuery;
import com.dogac.cart_service.domain.cart.Cart;
import com.dogac.cart_service.domain.cart.CartId;
import com.dogac.cart_service.domain.exceptions.CartNotFoundException;
import com.dogac.cart_service.domain.repositories.CartRepository;

@Component
public class GetCartByIdQueryHandler implements QueryHandler<GetCartByIdQuery, CartResponse> {

    private final CartRepository cartRepository;
    private final CartResponseMapper cartResponseMapper;

    public GetCartByIdQueryHandler(CartRepository cartRepository, CartResponseMapper cartResponseMapper) {
        this.cartRepository = cartRepository;
        this.cartResponseMapper = cartResponseMapper;
    }

    @Override
    public CartResponse handle(GetCartByIdQuery query) {
        Cart optionalCart = cartRepository.findById(CartId.from(query.cartId()))
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));

        return cartResponseMapper.toResponse(optionalCart);
    }
}
