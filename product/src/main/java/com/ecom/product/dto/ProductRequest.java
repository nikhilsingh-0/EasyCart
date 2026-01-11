package com.ecom.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {
    @NotBlank
    private String name;
    private String description;
    @PositiveOrZero
    private BigDecimal price;
    @PositiveOrZero
    private Integer stockQuantity;
    private Long sellerId;
    private Long category;
    private String imageUrl;
}
