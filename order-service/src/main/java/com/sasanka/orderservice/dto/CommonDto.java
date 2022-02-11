package com.sasanka.orderservice.dto;

import lombok.Data;
import java.util.List;
@Data
public class CommonDto{
    private CustomerDto customer;
    private OrderDto order;
    private List<ProductDto> products;

}
