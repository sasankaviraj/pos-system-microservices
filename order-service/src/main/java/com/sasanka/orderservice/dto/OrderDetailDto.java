package com.sasanka.orderservice.dto;

import lombok.Data;

@Data
public class OrderDetailDto extends ProductDto{

    private Long id;
    private Long orderId;
    private Long productId;
    private Integer quantity;
}
