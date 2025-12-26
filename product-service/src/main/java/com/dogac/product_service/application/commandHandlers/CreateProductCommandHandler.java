package com.dogac.product_service.application.commandHandlers;

import org.springframework.stereotype.Component;

import com.dogac.product_service.application.commands.CreateProductCommand;
import com.dogac.product_service.application.core.CommandHandler;
import com.dogac.product_service.application.dto.CreatedProductResponse;
import com.dogac.product_service.application.mapper.CreateProductMapper;
import com.dogac.product_service.domain.entities.Product;
import com.dogac.product_service.domain.repositories.ProductRepository;
import com.dogac.product_service.domain.services.ProductDomainService;
import com.dogac.product_service.domain.valueobjects.Description;
import com.dogac.product_service.domain.valueobjects.Money;
import com.dogac.product_service.domain.valueobjects.ProductName;
import com.dogac.product_service.domain.valueobjects.StockQuantity;

@Component
public class CreateProductCommandHandler
        implements CommandHandler<CreateProductCommand, CreatedProductResponse> {

    private final ProductDomainService productDomainService;
    private final ProductRepository productRepository;
    private final CreateProductMapper createProductMapper;

    public CreateProductCommandHandler(ProductDomainService productDomainService, ProductRepository productRepository,
            CreateProductMapper createProductMapper) {
        this.productDomainService = productDomainService;
        this.productRepository = productRepository;
        this.createProductMapper = createProductMapper;
    }

    @Override
    public CreatedProductResponse handle(CreateProductCommand command) {

        Money money = new Money(command.amount(), command.currency());
        StockQuantity stockQuantity = new StockQuantity(command.stockQuantity());

        Product product = productDomainService.createProduct(
                new ProductName(command.productName()),
                new Description(command.productDescription()),
                money,
                stockQuantity);

        productRepository.save(product);
        return createProductMapper.toResponse(product);
    }

}
