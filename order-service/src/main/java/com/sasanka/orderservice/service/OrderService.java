package com.sasanka.orderservice.service;

import com.sasanka.orderservice.dto.OrderDto;
import org.springframework.http.ResponseEntity;

public interface OrderService {

    ResponseEntity<?> saveOrder(OrderDto orderDto);

    ResponseEntity<?> findOrder(Long id);

    ResponseEntity<?> findOrderByCustomer(Long id);

    ResponseEntity<?> deleteOrder(Long id);
}
