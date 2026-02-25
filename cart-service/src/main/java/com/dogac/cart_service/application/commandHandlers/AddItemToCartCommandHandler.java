package com.dogac.cart_service.application.commandHandlers;

import org.springframework.stereotype.Component;

import com.dogac.cart_service.application.commands.AddItemToCartCommand;
import com.dogac.cart_service.application.core.CommandHandler;
import com.dogac.cart_service.domain.cart.Cart;
import com.dogac.cart_service.domain.repositories.CartRepository;
import com.dogac.cart_service.domain.valueobjects.Money;
import com.dogac.cart_service.domain.valueobjects.ProductId;
import com.dogac.cart_service.domain.valueobjects.Quantity;
import com.dogac.cart_service.domain.valueobjects.UserId;

@Component
public class AddItemToCartCommandHandler implements CommandHandler<AddItemToCartCommand, Void> {
    private final CartRepository cartRepository;

    public AddItemToCartCommandHandler(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public Void handle(AddItemToCartCommand command) {

        UserId userId = new UserId(command.userId());

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> Cart.create(
                        userId,
                        command.currency()));

        Quantity quantity = Quantity.of(command.quantity());
        ProductId productId = new ProductId(command.productId());
        Money money = Money.from(command.price(), command.currency());

        cart.addItem(productId, quantity, money);

        cartRepository.save(cart);

        return null;
    }

}
