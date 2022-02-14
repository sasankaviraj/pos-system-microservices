package com.sasanka.orderservice.service;

import com.sasanka.orderservice.dto.CommonDto;
import com.sasanka.orderservice.dto.OrderDto;
import com.sasanka.orderservice.exception.POSSystemException;

import java.util.List;

public interface OrderService {

    OrderDto saveOrder(OrderDto orderDto) throws POSSystemException;

    CommonDto findOrder(Long id) throws POSSystemException;

    List<CommonDto> findOrderByCustomer(Long id) throws POSSystemException;

    Long deleteOrder(Long id) throws POSSystemException;

    Long updateStatus(Long id) throws POSSystemException;

    List<List<CommonDto>> getAllOrders() throws POSSystemException;
}
