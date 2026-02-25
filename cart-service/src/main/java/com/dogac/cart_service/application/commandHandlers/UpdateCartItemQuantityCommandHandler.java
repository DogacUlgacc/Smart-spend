package com.dogac.cart_service.application.commandHandlers;

import org.springframework.stereotype.Component;

import com.dogac.cart_service.application.commands.UpdateCartItemQuantityCommand;
import com.dogac.cart_service.application.core.CommandHandler;
import com.dogac.cart_service.application.dto.CartResponse;
import com.dogac.cart_service.application.mapper.CartResponseMapper;
import com.dogac.cart_service.domain.cart.Cart;
import com.dogac.cart_service.domain.cart.CartId;
import com.dogac.cart_service.domain.exceptions.CartNotFoundException;
import com.dogac.cart_service.domain.repositories.CartRepository;
import com.dogac.cart_service.domain.valueobjects.ProductId;
import com.dogac.cart_service.domain.valueobjects.Quantity;

@Component
public class UpdateCartItemQuantityCommandHandler
        implements CommandHandler<UpdateCartItemQuantityCommand, CartResponse> {

    private final CartRepository cartRepository;
    private final CartResponseMapper cartResponseMapper;

    public UpdateCartItemQuantityCommandHandler(CartRepository cartRepository, CartResponseMapper cartResponseMapper) {
        this.cartRepository = cartRepository;
        this.cartResponseMapper = cartResponseMapper;
    }

    // *TODO:: Şuan keycloack olmadığı için findByIdAndUserId() yerine findById
    // kullanıyoruz!*/
    @Override
    public CartResponse handle(UpdateCartItemQuantityCommand command) {
        Cart cart = cartRepository.findById(
                CartId.from(command.cartId()))
                .orElseThrow(() -> new CartNotFoundException("cart not found!"));
        System.out.println("buraya bak : " + cart.getId());
        cart.changeItemQuantity(ProductId.from(command.productId()), new Quantity(command.quantity()));
        System.out.println("buraya bak : " + cart.getId());

        cartRepository.save(cart);

        return cartResponseMapper.toResponse(cart);
    }

}
