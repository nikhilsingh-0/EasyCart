package com.ecom.notification.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class OrderCreatedEvent {
    private final Long orderId;
    private final Long userId;
    private final BigDecimal totalAmount;
    private final OrderStatus status;
    private final List<OrderItemDTO> items;
    private final LocalDateTime createdAt;
}
