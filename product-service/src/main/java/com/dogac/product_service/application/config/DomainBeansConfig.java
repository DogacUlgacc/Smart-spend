package com.dogac.product_service.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dogac.product_service.domain.repositories.ProductRepository;
import com.dogac.product_service.domain.services.ProductDomainService;

@Configuration
public class DomainBeansConfig {

    @Bean
    public ProductDomainService productDomainService(ProductRepository productRepository) {
        return new ProductDomainService(productRepository);
    }
}
