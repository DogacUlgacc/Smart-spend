package com.dogac.product_service.infrastructure.kafka.cart;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dogac.common_events.event.CartItemAddedEvent;
import com.dogac.product_service.domain.entities.Product;
import com.dogac.product_service.domain.exceptions.ProductNotFoundException;
import com.dogac.product_service.domain.repositories.ProductRepository;
import com.dogac.product_service.domain.services.ProductDomainService;
import com.dogac.product_service.domain.valueobjects.ProductId;
import com.dogac.product_service.domain.valueobjects.StockQuantity;

@Component
public class CartItemAddedEventListener {
    private final ProductDomainService productDomainService;
    private final ProductRepository productRepository;

    public CartItemAddedEventListener(ProductDomainService productDomainService, ProductRepository productRepository) {
        this.productDomainService = productDomainService;
        this.productRepository = productRepository;
    }

    @KafkaListener(topics = "cart-item-added", groupId = "product-service")
    @Transactional
    public void handleCartItemAdded(CartItemAddedEvent event) {
        Product product = productRepository.findById(ProductId.from(event.productId()))
                .orElseThrow(() -> new ProductNotFoundException("Product bulunamadı."));
        productDomainService.reserveStock(product, StockQuantity.from(event.quantity()));
        productRepository.save(product);
    }
}
