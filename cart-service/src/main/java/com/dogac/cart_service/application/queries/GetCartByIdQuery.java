package com.dogac.cart_service.application.queries;

import java.util.UUID;

import com.dogac.cart_service.application.core.Query;
import com.dogac.cart_service.application.dto.CartResponse;

public record GetCartByIdQuery(UUID cartId) implements Query<CartResponse> {

}
