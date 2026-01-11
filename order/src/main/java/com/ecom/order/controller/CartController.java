package com.ecom.order.controller;


import com.ecom.order.dto.CartItemRequest;
import com.ecom.order.dto.CartResponse;
import com.ecom.order.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<Void> addToCart(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody CartItemRequest request) {

        boolean result = cartService.addToCart(userId, request);
        return result?ResponseEntity.status(HttpStatus.CREATED).build():ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeFromCart(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long productId) {

        cartService.removeItemFromCart(userId, productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart(
            @RequestHeader("X-User-Id") Long userId) {

        return ResponseEntity.ok(cartService.getCart(userId));
    }
}

