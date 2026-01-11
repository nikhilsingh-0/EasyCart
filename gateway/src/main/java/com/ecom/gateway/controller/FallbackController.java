package com.ecom.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    // PRODUCT SERVICE FALLBACK
    @GetMapping("/products")
    public Mono<ResponseEntity<Map<String, Object>>> productFallback() {

        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "Product service is temporarily unavailable");
        response.put("data", Collections.emptyList());

        return Mono.just(ResponseEntity
                        .status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body(response));
    }

    // ORDER SERVICE FALLBACK
    @GetMapping("/orders")
    public Mono<ResponseEntity<Map<String, Object>>> orderFallback() {

        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "Order service is temporarily unavailable");
        response.put("orderStatus", "PENDING");

        return Mono.just(ResponseEntity
                        .status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body(response));
    }
}
