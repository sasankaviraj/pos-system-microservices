package com.sasanka.orderservice.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class OrderDto {
    private Long orderId;
    private Long customerId;
    private boolean completed;
    private LocalDate dateTime;
    private List<OrderDetailDto> orderDetailList;
}
