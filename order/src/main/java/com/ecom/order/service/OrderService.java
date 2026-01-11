package com.ecom.order.service;


import com.ecom.order.client.UserServiceClient;
import com.ecom.order.dto.OrderItemDTO;
import com.ecom.order.dto.OrderResponse;
import com.ecom.order.dto.UserResponse;
import com.ecom.order.exception.EmptyCartException;
import com.ecom.order.exception.UserNotFoundException;
import com.ecom.order.kafka.OrderEventPublisher;
import com.ecom.order.model.Cart;
import com.ecom.order.model.Order;
import com.ecom.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final UserServiceClient userServiceClient;
    private final OrderEventPublisher eventPublisher;

    public OrderResponse createOrder(Long userId) {

        //Validate user
        UserResponse userResponse = userServiceClient.getUserDetails(userId);
        if (userResponse==null){
            throw new UserNotFoundException("username not found");
        }


        //Fetch cart
        Cart cart = cartService.getCartEntity(userId);
        if (cart.getItems().isEmpty()) {
            throw new EmptyCartException("Cart is empty");
        }

        // Create order aggregate
        Order order = new Order(userId);

        cart.getItems().forEach(item ->
                order.addItem(
                        item.getProductId(),
                        item.getQuantity(),
                        item.getPrice()
                )
        );

        order.confirm();

        //Persist
        Order savedOrder = orderRepository.save(order);

        //Clear cart
        cartService.clearCart(userId);

//        Publish event
         eventPublisher.publishOrderCreated(savedOrder);

        return mapToOrderResponse(savedOrder);
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getItems().stream()
                        .map(i -> new OrderItemDTO(
                                i.getId(),
                                i.getProductId(),
                                i.getQuantity(),
                                i.getPrice(),
                                i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity()))
                        ))
                        .toList(),
                order.getCreatedAt()
        );
    }
}


