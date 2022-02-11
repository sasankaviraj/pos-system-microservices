package com.sasanka.orderservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderDto {
    private Long orderId;
    private Long customerId;
    private List<OrderDetailDto> orderDetailList;
}
