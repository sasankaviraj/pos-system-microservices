package com.sasanka.productservice.service.impl;

import com.sasanka.productservice.dto.ProductDto;
import com.sasanka.productservice.entity.Product;
import com.sasanka.productservice.repository.ProductRepository;
import com.sasanka.productservice.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Override
    public ResponseEntity<?> saveProduct(ProductDto productDto) {
        Product product = new Product();
        BeanUtils.copyProperties(productDto,product);
        Product saved = productRepository.save(product);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateProduct(ProductDto productDto, Long id) {
        Product byId = productRepository.getById(id);
        BeanUtils.copyProperties(productDto,byId);
        byId.setId(id);
        Product saved = productRepository.save(byId);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteProduct(Long id) {
        Product byId = productRepository.getById(id);
        productRepository.delete(byId);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @Override
    public ResponseEntity<?> getAllProducts() {
        List<Product> all = productRepository.findAll();
        return new ResponseEntity<>(all,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> findById(Long id) {
        Optional<Product> byId = productRepository.findById(id);
        return new ResponseEntity<>(byId.isPresent() ? byId : "Nothing to show",HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> findByIds(List<Long> ids) {
        List<Product> allById = productRepository.findAllById(ids);
        return new ResponseEntity<>(allById,HttpStatus.OK);

    }
}
