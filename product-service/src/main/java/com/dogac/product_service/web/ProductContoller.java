package com.dogac.product_service.web;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dogac.product_service.application.commandHandlers.CreateProductCommandHandler;
import com.dogac.product_service.application.commands.CreateProductCommand;
import com.dogac.product_service.application.dto.CreatedProductResponse;
import com.dogac.product_service.application.dto.ProductResponse;
import com.dogac.product_service.application.queries.GetProductByIdQuery;
import com.dogac.product_service.application.queryHandlers.GetProductByIdQueryHandler;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/products")
public class ProductContoller {

    private final CreateProductCommandHandler createProductCommandHandler;
    private final GetProductByIdQueryHandler getProductByIdQueryHandler;

    public ProductContoller(CreateProductCommandHandler createProductCommandHandler,
            GetProductByIdQueryHandler getProductByIdQueryHandler) {
        this.createProductCommandHandler = createProductCommandHandler;
        this.getProductByIdQueryHandler = getProductByIdQueryHandler;
    }

    @PostMapping
    public ResponseEntity<CreatedProductResponse> createProduct(
            @Valid @RequestBody CreateProductCommand command) {

        CreatedProductResponse response = createProductCommandHandler.handle(command);

        URI location = URI.create("/products/" + response.id());

        return ResponseEntity
                .created(location)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable UUID id) {
        ProductResponse response = getProductByIdQueryHandler.handle(new GetProductByIdQuery(id));
        return ResponseEntity.ok(response);
    }

}
