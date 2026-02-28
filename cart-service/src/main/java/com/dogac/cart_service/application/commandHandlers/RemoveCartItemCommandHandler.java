package com.dogac.cart_service.application.commandHandlers;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dogac.cart_service.application.commands.RemoveCartItemCommand;
import com.dogac.cart_service.application.core.CommandHandler;
import com.dogac.cart_service.domain.cart.Cart;
import com.dogac.cart_service.domain.cart.CartId;
import com.dogac.cart_service.domain.exceptions.CartNotFoundException;
import com.dogac.cart_service.domain.repositories.CartRepository;
import com.dogac.cart_service.domain.valueobjects.ProductId;

@Component
public class RemoveCartItemCommandHandler implements CommandHandler<RemoveCartItemCommand, Void> {

    private final CartRepository cartRepository;

    public RemoveCartItemCommandHandler(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    @Transactional
    public Void handle(RemoveCartItemCommand command) {

        Cart cart = cartRepository.findById(CartId.from(command.cartId()))
                .orElseThrow(() -> new CartNotFoundException("Cart with given id not found!"));
        cart.removeItem(ProductId.from(command.productId()));
        cartRepository.save(cart);
        return null;

    }

}
