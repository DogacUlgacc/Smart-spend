package com.dogac.cart_service.application.mapper;

import org.mapstruct.Mapper;

import com.dogac.cart_service.application.dto.CreatedCartResponse;
import com.dogac.cart_service.domain.cart.Cart;

@Mapper(componentModel = "spring")
public interface CreatedCartResponseMapper {

    default CreatedCartResponse toResponse(Cart cart) {
        return new CreatedCartResponse(
                cart.getId().value(),
                cart.getUserId().value(),
                cart.getCurrency());
    }
}
