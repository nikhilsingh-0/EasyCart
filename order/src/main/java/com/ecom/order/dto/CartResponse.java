package com.ecom.order.dto;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(
        Long cartId,
        Long userId,
        BigDecimal totalAmount,
        List<CartItemResponse> items
) {}
