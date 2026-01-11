package com.ecom.gateway.security;


import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthValidationFilter implements GlobalFilter, Ordered {

    private final JwtService jwtService;

    public JwtAuthValidationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        ServerHttpRequest sanitizedRequest = exchange.getRequest()
                .mutate()
                .headers(headers -> {
                    headers.remove("X-User-Id");
                    headers.remove("X-Role");
                })
                .build();

        //Public APIs
        if (path.startsWith("/api/users/login")
                || path.startsWith("/api/users/register")
                || path.startsWith("/api/products")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorized(exchange, "Missing Authorization Header");
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = jwtService.validateToken(token);

            Long userId = claims.get("userId", Long.class);
            String role = claims.get("role", String.class);

            ServerHttpRequest mutatedRequest = exchange.getRequest()
                    .mutate()
                    .header("X-User-Id", String.valueOf(userId))
                    .header("X-User-Roles", role)
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());

        } catch (Exception ex) {
            return unauthorized(exchange, "Invalid or Expired Token");
        }
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}

