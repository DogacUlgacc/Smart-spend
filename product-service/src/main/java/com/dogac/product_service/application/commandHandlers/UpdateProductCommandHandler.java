package com.dogac.product_service.application.commandHandlers;

import org.springframework.stereotype.Component;

import com.dogac.product_service.application.commands.UpdateProductCommand;
import com.dogac.product_service.application.core.CommandHandler;
import com.dogac.product_service.application.dto.UpdatedProductResponse;
import com.dogac.product_service.application.mapper.UpdateProductMapper;
import com.dogac.product_service.domain.entities.Product;
import com.dogac.product_service.domain.repositories.ProductRepository;
import com.dogac.product_service.domain.services.ProductDomainService;
import com.dogac.product_service.domain.valueobjects.Description;
import com.dogac.product_service.domain.valueobjects.Money;
import com.dogac.product_service.domain.valueobjects.ProductId;
import com.dogac.product_service.domain.valueobjects.ProductName;
import com.dogac.product_service.domain.valueobjects.StockQuantity;

import jakarta.transaction.Transactional;

@Component
public class UpdateProductCommandHandler implements CommandHandler<UpdateProductCommand, UpdatedProductResponse> {

    private final ProductDomainService productDomainService;
    private final ProductRepository productRepository;
    private final UpdateProductMapper updateProductMapper;

    public UpdateProductCommandHandler(ProductDomainService productDomainService, ProductRepository productRepository,
            UpdateProductMapper updateProductMapper) {
        this.productDomainService = productDomainService;
        this.productRepository = productRepository;
        this.updateProductMapper = updateProductMapper;
    }

    @Override
    @Transactional
    public UpdatedProductResponse handle(UpdateProductCommand command) {
        ProductId productId = new ProductId(command.id());
        Product updatedProduct = productDomainService.updateProduct(productId,
                new ProductName(command.productName()), new Description(command.productDescription()),
                new Money(command.amount(), command.currency()),
                new StockQuantity(command.stockQuantity()));

        updatedProduct = productRepository.save(updatedProduct);
        return updateProductMapper.toResponse(updatedProduct);
    }

}
