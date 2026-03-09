package com.dogac.product_service.infrastructure.kafka.cart.dto;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dogac.common_events.event.CartItemRemovedEvent;
import com.dogac.product_service.domain.entities.Product;
import com.dogac.product_service.domain.exceptions.ProductNotFoundException;
import com.dogac.product_service.domain.repositories.ProductRepository;
import com.dogac.product_service.domain.services.ProductDomainService;
import com.dogac.product_service.domain.valueobjects.ProductId;
import com.dogac.product_service.domain.valueobjects.StockQuantity;

@Component
public class CartItemRemovedEventListener {

    private final ProductDomainService productDomainService;
    private final ProductRepository productRepository;

    public CartItemRemovedEventListener(ProductDomainService productDomainService,
            ProductRepository productRepository) {
        this.productDomainService = productDomainService;
        this.productRepository = productRepository;
    }

    @KafkaListener(topics = "cart-item-removed", groupId = "product-service")
    @Transactional
    public void handleCartItemRemoved(CartItemRemovedEvent event) {
        Product product = productRepository.findById(ProductId.from(event.productId()))
                .orElseThrow(() -> new ProductNotFoundException("Product bulunamadı."));
        productDomainService.releaseStock(product, StockQuantity.from(event.quantity()));
        productRepository.save(product);
    }
}

