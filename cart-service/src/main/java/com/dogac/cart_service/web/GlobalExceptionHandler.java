package com.dogac.cart_service.web;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dogac.cart_service.application.exception.CartWithGivenUserIdNotFound;
import com.dogac.cart_service.application.exception.NotEnoughStockException;
import com.dogac.cart_service.domain.exceptions.CartNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(CartWithGivenUserIdNotFound.class)
    public ResponseEntity<String> handleCartNotFound(CartWithGivenUserIdNotFound ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(NotEnoughStockException.class)
    public ResponseEntity<String> handleNotEnoughStock(NotEnoughStockException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<String> handleCartNotFoundException(CartNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception ex) {
        ex.printStackTrace(); // log
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unexpected error occurred");
    }

    /* Just for NotFound ex */
    @ExceptionHandler(feign.FeignException.NotFound.class)
    public ResponseEntity<Map<String, Object>> handleFeignNotFound(feign.FeignException.NotFound ex) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", 404);
        body.put("error", "Not Found");
        body.put("message", "Product with given id not found");

        return ResponseEntity.status(404).body(body);
    }
}
