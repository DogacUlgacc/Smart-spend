package com.dogac.cart_service.application.commandHandlers;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dogac.cart_service.application.commands.RemoveCartItemCommand;
import com.dogac.cart_service.application.core.CommandHandler;
import com.dogac.cart_service.domain.cart.Cart;
import com.dogac.cart_service.domain.cart.CartId;
import com.dogac.cart_service.domain.cart.CartItem;
import com.dogac.cart_service.domain.exceptions.CartNotFoundException;
import com.dogac.cart_service.domain.repositories.CartRepository;
import com.dogac.cart_service.domain.valueobjects.ProductId;
import com.dogac.cart_service.domain.valueobjects.Quantity;
import com.dogac.common_events.event.CartItemRemovedEvent;

@Component
public class RemoveCartItemCommandHandler implements CommandHandler<RemoveCartItemCommand, Void> {

    private final CartRepository cartRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public RemoveCartItemCommandHandler(CartRepository cartRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.cartRepository = cartRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    @Transactional
    public Void handle(RemoveCartItemCommand command) {

        Cart cart = cartRepository.findById(CartId.from(command.cartId()))
                .orElseThrow(() -> new CartNotFoundException("Cart with given id not found!"));

        Quantity quantityToReturn = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(ProductId.from(command.productId())))
                .map(CartItem::getQuantity)
                .findFirst()
                .orElseThrow(() -> new CartNotFoundException("Cart item not found!"));
        Integer integerQuantityToReturn = Quantity.toInteger(quantityToReturn);
        cart.removeItem(ProductId.from(command.productId()));
        cartRepository.save(cart);

        CartItemRemovedEvent event = new CartItemRemovedEvent(
                command.cartId(),
                command.productId(),
                integerQuantityToReturn);
        kafkaTemplate.send("cart-item-removed", event);
        return null;

    }

}
