package com.dogac.cart_service.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dogac.cart_service.application.bus.CommandBus;
import com.dogac.cart_service.application.bus.QueryBus;
import com.dogac.cart_service.application.commands.AddItemToCartCommand;
import com.dogac.cart_service.application.commands.CreateCartCommand;
import com.dogac.cart_service.application.dto.CreatedCartResponse;

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
}