package com.dogac.cart_service.application.commandHandlers;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dogac.cart_service.application.commands.CreateCartCommand;
import com.dogac.cart_service.application.core.CommandHandler;
import com.dogac.cart_service.application.dto.CreatedCartResponse;
import com.dogac.cart_service.application.mapper.CreatedCartResponseMapper;
import com.dogac.cart_service.domain.cart.Cart;
import com.dogac.cart_service.domain.enums.CurrencyType;
import com.dogac.cart_service.domain.repositories.CartRepository;
import com.dogac.cart_service.domain.valueobjects.UserId;

@Component
public class CreateCartCommandHandler implements CommandHandler<CreateCartCommand, CreatedCartResponse> {

    private final CartRepository cartRepository;
    private final CreatedCartResponseMapper createdCartResponseMapper;

    public CreateCartCommandHandler(CartRepository cartRepository,
            CreatedCartResponseMapper createdCartResponseMapper) {
        this.cartRepository = cartRepository;
        this.createdCartResponseMapper = createdCartResponseMapper;
    }

    @Override
    @Transactional
    public CreatedCartResponse handle(CreateCartCommand command) {
        UserId userId = UserId.from(command.userId());
        CurrencyType currencyType = command.currency();

        Cart cart = Cart.create(userId, currencyType);
        cartRepository.save(cart);
        return createdCartResponseMapper.toResponse(cart);
    }
}
