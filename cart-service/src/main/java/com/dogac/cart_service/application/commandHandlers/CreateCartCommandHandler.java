package com.dogac.cart_service.application.commandHandlers;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dogac.cart_service.application.commands.CreateCartCommand;
import com.dogac.cart_service.application.core.CommandHandler;
import com.dogac.cart_service.application.dto.CreatedCartResponse;
import com.dogac.cart_service.application.mapper.CreatedCartResponseMapper;
import com.dogac.cart_service.application.security.CurrentUserService;
import com.dogac.cart_service.domain.cart.Cart;
import com.dogac.cart_service.domain.enums.Currency;
import com.dogac.cart_service.domain.repositories.CartRepository;
import com.dogac.cart_service.domain.valueobjects.UserId;

@Component
public class CreateCartCommandHandler implements CommandHandler<CreateCartCommand, CreatedCartResponse> {

    private final CartRepository cartRepository;
    private final CreatedCartResponseMapper createdCartResponseMapper;
    private final CurrentUserService currentUserService;

    public CreateCartCommandHandler(CartRepository cartRepository, CreatedCartResponseMapper createdCartResponseMapper,
            CurrentUserService currentUserService) {
        this.cartRepository = cartRepository;
        this.createdCartResponseMapper = createdCartResponseMapper;
        this.currentUserService = currentUserService;
    }

    @Override
    @Transactional
    public CreatedCartResponse handle(CreateCartCommand command) {
        UserId userId = currentUserService.getUserId();
        Currency currencyType = command.currency();

        Optional<Cart> existingCart = cartRepository.findByUserId(userId);
        if (existingCart.isPresent()) {
            return createdCartResponseMapper.toResponse(existingCart.get());
        }

        Cart cart = Cart.create(userId, currencyType);
        cartRepository.save(cart);
        return createdCartResponseMapper.toResponse(cart);
    }
}
