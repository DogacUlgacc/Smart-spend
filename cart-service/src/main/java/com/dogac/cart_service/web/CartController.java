package com.dogac.cart_service.web;

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

import com.dogac.cart_service.application.bus.CommandBus;
import com.dogac.cart_service.application.bus.QueryBus;
import com.dogac.cart_service.application.commands.AddItemToCartCommand;
import com.dogac.cart_service.application.commands.CreateCartCommand;
import com.dogac.cart_service.application.commands.RemoveCartItemCommand;
import com.dogac.cart_service.application.commands.UpdateCartItemQuantityCommand;
import com.dogac.cart_service.application.dto.CartResponse;
import com.dogac.cart_service.application.dto.CreatedCartResponse;
import com.dogac.cart_service.application.dto.UpdateQuantityRequest;
import com.dogac.cart_service.application.queries.GetCartByIdQuery;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/carts")
public class CartController {

    private final CommandBus commandBus;
    private final QueryBus queryBus;

    public CartController(CommandBus commandBus, QueryBus queryBus) {
        this.commandBus = commandBus;
        this.queryBus = queryBus;
    }

    @PostMapping
    public ResponseEntity<CreatedCartResponse> createCart(@RequestBody @Valid CreateCartCommand command) {
        CreatedCartResponse response = commandBus.send(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/items")
    public ResponseEntity<Void> addItem(@RequestBody AddItemToCartCommand command) {
        commandBus.send(command);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable UUID cartId) {
        CartResponse response = queryBus.execute(new GetCartByIdQuery(cartId));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<CartResponse> updateQuantity(
            @PathVariable UUID cartId,
            @PathVariable UUID productId,
            @Valid @RequestBody UpdateQuantityRequest request) {

        // TODO: Bu metodu keycloak entegrasyonundan sonra güncelle!
        UUID userId = UUID.randomUUID();
        CartResponse response = commandBus.send(
                new UpdateCartItemQuantityCommand(
                        cartId,
                        userId,
                        productId,
                        request.quantity()));

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<Void> deleteItem(
            @PathVariable UUID cartId,
            @PathVariable UUID productId) {
        RemoveCartItemCommand command = new RemoveCartItemCommand(cartId, productId);
        commandBus.send(command);
        return ResponseEntity.noContent().build();
    }
}