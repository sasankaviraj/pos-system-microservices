package com.sasanka.orderservice.dto;

import lombok.Data;

@Data
public class OrderDetailDto {
    private Long id;
    private Long orderId;
    private Long productId;
    private Integer quantity;
}
