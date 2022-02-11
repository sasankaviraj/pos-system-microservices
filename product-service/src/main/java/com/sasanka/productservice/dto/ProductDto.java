package com.sasanka.productservice.dto;

import lombok.Data;

@Data
public class ProductDto {

    private Long id;
    private String name;
    private Double price;
    private Double weight;
}
