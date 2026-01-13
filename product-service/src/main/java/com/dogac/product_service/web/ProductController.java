package com.dogac.product_service.web;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dogac.product_service.application.bus.CommandBus;
import com.dogac.product_service.application.bus.QueryBus;
import com.dogac.product_service.application.commands.CreateProductCommand;
import com.dogac.product_service.application.commands.DeleteProductCommand;
import com.dogac.product_service.application.commands.UpdateProductCommand;
import com.dogac.product_service.application.dto.CreatedProductResponse;
import com.dogac.product_service.application.dto.ProductResponse;
import com.dogac.product_service.application.dto.UpdateProductRequest;
import com.dogac.product_service.application.dto.UpdatedProductResponse;
import com.dogac.product_service.application.queries.GetProductByIdQuery;
import com.dogac.product_service.application.queries.GetProductListQuery;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    private final CommandBus commandBus;
    private final QueryBus queryBus;

    public ProductController(CommandBus commandBus, QueryBus queryBus) {
        this.commandBus = commandBus;
        this.queryBus = queryBus;
    }

    @PostMapping
    public ResponseEntity<CreatedProductResponse> createProduct(
            @Valid @RequestBody CreateProductCommand command) {
        CreatedProductResponse response = commandBus.send(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable UUID id) {
        GetProductByIdQuery query = new GetProductByIdQuery(id);
        ProductResponse response = queryBus.execute(query);
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<List<ProductResponse>> getProductList() {
        GetProductListQuery query = new GetProductListQuery();
        List<ProductResponse> responseList = queryBus.execute(query);
        return ResponseEntity.ok(responseList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdatedProductResponse> updateProduct(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateProductRequest request) {

        UpdateProductCommand command = new UpdateProductCommand(id,
                request.productName(),
                request.productDescription(),
                request.amount(),
                request.currency(),
                request.stockQuantity());
        UpdatedProductResponse response = commandBus.send(command);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        commandBus.send(new DeleteProductCommand(id));
        return ResponseEntity.noContent().build();
    }
}
