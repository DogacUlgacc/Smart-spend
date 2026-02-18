package com.dogac.cart_service.application.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import com.dogac.cart_service.application.dto.CartItemResponse;
import com.dogac.cart_service.application.dto.CartResponse;
import com.dogac.cart_service.domain.cart.Cart;
import com.dogac.cart_service.domain.cart.CartItem;

@Component
@Mapper(componentModel = "spring")
public interface CartResponseMapper {

    default CartResponse toResponse(Cart cart) {

        return new CartResponse(cart.getId().value(), cart.getUserId().value(), cart.getCurrency().name(),
                cart.getItems().stream().map(this::toItemResponse).toList(), cart.totalAmount().amount());
    }

    default CartItemResponse toItemResponse(CartItem item) {
        return new CartItemResponse(
                item.getProductId().value(),
                item.getQuantity().value(),
                item.getPrice().amount(),
                item.totalPrice().amount());
    }
}