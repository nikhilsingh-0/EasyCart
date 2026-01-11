package com.ecom.order.dto;

import java.math.BigDecimal;

public record CartItemResponse(
        Long productId,
        Integer quantity,
        BigDecimal price,
        BigDecimal subTotal
) {}

