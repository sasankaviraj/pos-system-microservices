package com.sasanka.productservice.service;

import com.sasanka.productservice.dto.ProductDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {

    ResponseEntity<?> saveProduct(ProductDto productDto);

    ResponseEntity<?> updateProduct(ProductDto productDto, Long id);

    ResponseEntity<?> deleteProduct(Long id);

    ResponseEntity<?> getAllProducts();

    ResponseEntity<?> findById(Long id);

    ResponseEntity<?> findByIds(List<Long> ids);
}
