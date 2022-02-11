package com.sasanka.orderservice.dto;

import lombok.Data;

@Data
public class CustomerDto {
    private Long id;
    private String name;
    private String address;
    private String contactNumber;
}
