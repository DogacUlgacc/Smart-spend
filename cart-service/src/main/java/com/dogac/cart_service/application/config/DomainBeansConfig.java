package com.dogac.cart_service.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dogac.cart_service.domain.repositories.CartRepository;
import com.dogac.cart_service.domain.services.CartDomainService;

@Configuration
public class DomainBeansConfig {

    @Bean
    public CartDomainService cartDomainService(CartRepository cartRepository) {
        return new CartDomainService(cartRepository);
    }
}
