package com.dogac.cart_service.application.commandHandlers;

import org.springframework.stereotype.Component;

import com.dogac.cart_service.application.commands.AddItemToCartCommand;
import com.dogac.cart_service.application.core.CommandHandler;
import com.dogac.cart_service.application.dto.feignDto.ProductDto;
import com.dogac.cart_service.application.exception.NotEnoughStockException;
import com.dogac.cart_service.application.port.ProductPort;
import com.dogac.cart_service.domain.cart.Cart;
import com.dogac.cart_service.domain.exceptions.CartNotFoundException;
import com.dogac.cart_service.domain.repositories.CartRepository;
import com.dogac.cart_service.domain.valueobjects.Money;
import com.dogac.cart_service.domain.valueobjects.ProductId;
import com.dogac.cart_service.domain.valueobjects.Quantity;
import com.dogac.cart_service.domain.valueobjects.UserId;

@Component
public class AddItemToCartCommandHandler implements CommandHandler<AddItemToCartCommand, Void> {
    private final CartRepository cartRepository;
    private final ProductPort productPort;

    public AddItemToCartCommandHandler(CartRepository cartRepository, ProductPort productPort) {
        this.cartRepository = cartRepository;
        this.productPort = productPort;
    }

    @Override
    public Void handle(AddItemToCartCommand command) {

        UserId userId = new UserId(command.userId());
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new CartNotFoundException("No cart found"));

        ProductDto product = productPort.getProductById(command.productId());

        if (product.stockQuantity() < command.quantity()) {
            throw new NotEnoughStockException("Stock not enough!");
        }

        Quantity quantity = Quantity.of(command.quantity());
        ProductId productId = new ProductId(command.productId());

        Money money = Money.from(product.amount(), Money.toCurrencyEnum(product.currency()));

        cart.addItem(productId, quantity, money);

        cartRepository.save(cart);

        return null;
    }

}
