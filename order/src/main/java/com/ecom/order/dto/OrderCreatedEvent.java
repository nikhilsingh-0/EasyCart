package com.ecom.order.dto;

import com.ecom.order.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class OrderCreatedEvent {

    private final Long orderId;
    private final Long userId;
    private final BigDecimal totalAmount;
    private final OrderStatus status;
    private final List<OrderItemDTO> items;
    private final LocalDateTime createdAt;
}
