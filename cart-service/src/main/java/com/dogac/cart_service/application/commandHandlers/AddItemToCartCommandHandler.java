package com.dogac.cart_service.application.commandHandlers;

import org.springframework.stereotype.Component;

import com.dogac.cart_service.application.commands.AddItemToCartCommand;
import com.dogac.cart_service.application.core.CommandHandler;
import com.dogac.cart_service.application.dto.feignDto.ProductDto;
import com.dogac.cart_service.application.exception.NotEnoughStockException;
import com.dogac.cart_service.application.port.ProductPort;
import com.dogac.cart_service.application.security.CurrentUserService;
import com.dogac.cart_service.domain.cart.Cart;
import com.dogac.cart_service.domain.exceptions.CartNotFoundException;
import com.dogac.cart_service.domain.repositories.CartRepository;
import com.dogac.cart_service.domain.valueobjects.Money;
import com.dogac.cart_service.domain.valueobjects.ProductId;
import com.dogac.cart_service.domain.valueobjects.Quantity;
import com.dogac.cart_service.domain.valueobjects.UserId;
import com.dogac.cart_service.infrastructure.kafka.KafkaEventPublisher;
import com.dogac.common_events.enums.Currency;
import com.dogac.common_events.event.CartItemAddedEvent;

@Component
public class AddItemToCartCommandHandler implements CommandHandler<AddItemToCartCommand, Void> {
    private final CartRepository cartRepository;
    private final ProductPort productPort;
    private final KafkaEventPublisher kafkaEventPublisher;
    private final CurrentUserService currentUserService;

    public AddItemToCartCommandHandler(CartRepository cartRepository, ProductPort productPort,
            KafkaEventPublisher kafkaEventPublisher, CurrentUserService currentUserService) {
        this.cartRepository = cartRepository;
        this.productPort = productPort;
        this.kafkaEventPublisher = kafkaEventPublisher;
        this.currentUserService = currentUserService;
    }

    @Override
    public Void handle(AddItemToCartCommand command) {
        System.out.println("AddItemToCartCommand handle()");

        UserId userId = currentUserService.getUserId();
        System.out.println("currentUserService ile gelen userId: " + userId);

        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new CartNotFoundException("No cart found"));

        // Openfeign ile product-service'ten gelen product!
        ProductDto product = productPort.getProductById(command.productId());
        System.out.println("product serviceten gelen productDto: " + product);
        // Double check for quantity
        if (product.stockQuantity() < command.quantity()) {
            throw new NotEnoughStockException("Stock not enough!");
        }

        Quantity quantity = Quantity.of(command.quantity());
        ProductId productId = new ProductId(command.productId());

        Money money = Money.from(product.amount(), Money.toCurrencyEnum(product.currency()));

        cart.addItem(productId, quantity, money);
        cartRepository.save(cart);

        // Event publish
        CartItemAddedEvent event = new CartItemAddedEvent(
                cart.getId().value(),
                cart.getUserId().value(),
                productId.value(),
                command.quantity(),
                money.amount(),
                Currency.valueOf(cart.getCurrency().name()));

        kafkaEventPublisher.publishCartItemAdded(event);

        return null;
    }

}
