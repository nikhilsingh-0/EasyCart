package com.ecom.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

@Configuration
public class GatewayConfig {

//    @Bean
//    public RedisRateLimiter redisRateLimiter() {
//        return new RedisRateLimiter(10,20,1);
//    }

    @Bean
    public KeyResolver hostNameKeyResolver() {
        return exchange ->
                Mono.justOrEmpty(exchange.getRequest()
                                .getHeaders()
                                .getFirst("X-User-Id"))
                        .defaultIfEmpty("anonymous");
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Product Service
                .route("product-service", r -> r
                        .path("/api/products/**")
                        .filters(f -> f.retry(retryConfig -> retryConfig
                                        .setRetries(3)
                                        .setMethods(HttpMethod.GET)
                                )
//                                .requestRateLimiter(config -> config
//                                        .setRateLimiter(redisRateLimiter())
//                                        .setKeyResolver(hostNameKeyResolver()))
//                                .circuitBreaker(config -> config
//                                        .setName("productBreaker")
//                                        .setFallbackUri("forward:/fallback/products")))
                        )
                        .uri("lb://PRODUCT"))
                // User Service
                .route("user-service", r -> r
                        .path("/api/users/**")
                        .uri("lb://USER"))

                // Order Service
                .route("order-service", r -> r
                        .path("/api/orders/**", "/api/cart/**")
                        .filters(f -> f
                                .circuitBreaker(config -> config
                                        .setName("orderBreaker")
                                        .setFallbackUri("forward:/fallback/orders")))
                        .uri("lb://ORDER"))
                // Eureka
                .route("eureka-server", r -> r
                        .path("/eureka/main")
                        .filters(f -> f.rewritePath("/eureka/main", "/"))
                        .uri("http://localhost:8761"))
                .route("eureka-server-static", r -> r
                        .path("/eureka/**")
                        .uri("http://localhost:8761"))
                .build();
    }
}
