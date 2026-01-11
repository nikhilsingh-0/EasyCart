package com.ecom.order.dto;

import lombok.Builder;

@Builder
public class ExceptionResponseDTO {
    private int status;
    private String message;
    private String path;
    private String timestamp;
    private final String SERVICE="order-service";
}
