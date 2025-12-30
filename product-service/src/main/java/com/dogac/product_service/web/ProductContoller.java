package com.dogac.product_service.web;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dogac.product_service.application.commandHandlers.CreateProductCommandHandler;
import com.dogac.product_service.application.commandHandlers.UpdateProductCommandHandler;
import com.dogac.product_service.application.commands.CreateProductCommand;
import com.dogac.product_service.application.commands.UpdateProductCommand;
import com.dogac.product_service.application.dto.CreatedProductResponse;
import com.dogac.product_service.application.dto.ProductResponse;
import com.dogac.product_service.application.dto.UpdateProductRequest;
import com.dogac.product_service.application.dto.UpdatedProductResponse;
import com.dogac.product_service.application.queries.GetProductByIdQuery;
import com.dogac.product_service.application.queries.GetProductListQuery;
import com.dogac.product_service.application.queryHandlers.GetProductByIdQueryHandler;
import com.dogac.product_service.application.queryHandlers.GetProductListQueryHandler;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/products")
public class ProductContoller {

    private final CreateProductCommandHandler createProductCommandHandler;
    private final GetProductByIdQueryHandler getProductByIdQueryHandler;
    private final GetProductListQueryHandler getProductListQueryHandler;
    private final UpdateProductCommandHandler updateProductCommandHandler;

    public ProductContoller(CreateProductCommandHandler createProductCommandHandler,
            GetProductByIdQueryHandler getProductByIdQueryHandler,
            GetProductListQueryHandler getProductListQueryHandler,
            UpdateProductCommandHandler updateProductCommandHandler) {
        this.createProductCommandHandler = createProductCommandHandler;
        this.getProductByIdQueryHandler = getProductByIdQueryHandler;
        this.getProductListQueryHandler = getProductListQueryHandler;
        this.updateProductCommandHandler = updateProductCommandHandler;
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

    @GetMapping("/all")
    public ResponseEntity<List<ProductResponse>> getProductList() {
        List<ProductResponse> responseList = getProductListQueryHandler.handle(new GetProductListQuery());
        return ResponseEntity.ok(responseList);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UpdatedProductResponse> updateProduct(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateProductRequest request) {

        UpdateProductCommand command = new UpdateProductCommand(id,
                request.productName(),
                request.productDescription(),
                request.amount(),
                request.currency(),
                request.stockQuantity());
        updateProductCommandHandler.handle(command);
        return ResponseEntity.ok().build();
    }

}
