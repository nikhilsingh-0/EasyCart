package com.ecom.order.service;


import com.ecom.order.client.ProductServiceClient;
import com.ecom.order.client.UserServiceClient;
import com.ecom.order.dto.*;
import com.ecom.order.exception.CartNotFoundException;
import com.ecom.order.exception.ProductNotFoundException;
import com.ecom.order.exception.UserNotFoundException;
import com.ecom.order.model.Cart;
import com.ecom.order.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final ProductServiceClient productServiceClient;
    private final UserServiceClient userServiceClient;

//    @CircuitBreaker(name = "orderService", fallbackMethod = "addToCartFallback")
//    @Retry(name = "retryBreaker")
    public boolean addToCart(Long userId, CartItemRequest request) {

        //Validate user
        UserResponse response = userServiceClient.getUserDetails(userId);
        if (response==null){
            throw new UserNotFoundException("Username not found");
        }

        //Fetch product
        ProductResponse product =
                productServiceClient.getProductDetails(request.getProductId());

        if (product == null) {
            throw new ProductNotFoundException("Product not found");
        }

        //Load or create cart
        Cart cart = cartRepository
                .findByUserId(userId)
                .orElseGet(() -> cartRepository.save(new Cart(userId)));

        //Delegate to domain
        cart.addItem(
                product.getId(),
                request.getQuantity(),
                product.getPrice(),
                product.getStockQuantity()
        );

        //Persist aggregate root
        Cart savedCart = cartRepository.save(cart);

        return savedCart.getId()>0;
    }

    public void removeItemFromCart(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));
        cart.removeItem(productId);
        cartRepository.save(cart);
    }

    public Cart getCartEntity(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));
    }

    public CartResponse getCart(Long userId) {
        Cart cart = getCartEntity(userId);
        List<CartItemResponse> itemResponses = cart.getItems().stream()
                .map(item -> new CartItemResponse(
                        item.getProductId(),
                        item.getQuantity(),
                        item.getPrice(),
                        item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                ))
                .toList();

        return new CartResponse(
                cart.getId(),
                cart.getUserId(),
                cart.getTotalAmount(),
                itemResponses
        );
    }

    public void clearCart(Long userId) {
        Cart cart = getCartEntity(userId);
        cart.clear();
        cartRepository.save(cart);
    }

    public void addToCartFallback(
            Long userId,
            CartItemRequest request,
            Exception ex) {

        throw new IllegalStateException(
                "Unable to add item to cart. Please try again later", ex
        );
    }
}
