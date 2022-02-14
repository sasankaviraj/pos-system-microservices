package com.sasanka.productservice.controller;

import com.sasanka.productservice.dto.ProductDto;
import com.sasanka.productservice.service.ProductService;
import com.sasanka.productservice.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constants.BASE_URL)
@Slf4j
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping(value = "/save",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody ProductDto productDto){
        try {
            return productService.saveProduct(productDto);
        }catch (Exception exception){
            log.error(exception.getMessage());
            return new ResponseEntity(Constants.ERROR_POST_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@RequestBody ProductDto productDto,@PathVariable("id") Long id){
        try {
            return productService.updateProduct(productDto,id);
        }catch (Exception exception){
            log.error(exception.getMessage());
            return new ResponseEntity(Constants.ERROR_UPDATE_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        try {
            return productService.deleteProduct(id);
        }catch (Exception exception){
            log.error(exception.getMessage());
            return new ResponseEntity(Constants.ERROR_DELETE_MESSAGE,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAll(){
        try {
            return productService.getAllProducts();
        }catch (Exception exception){
            log.error(exception.getMessage());
            return new ResponseEntity(Constants.ERROR_GET_MESSAGE,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/find/{id}")
    public ResponseEntity<?> find(@PathVariable("id") Long id){
        try {
            return productService.findById(id);
        }catch (Exception exception){
            log.error(exception.getMessage());
            return new ResponseEntity(Constants.ERROR_GET_MESSAGE,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/findAll")
    public ResponseEntity<?> find(@RequestBody List<Long> ids){
        try {
            return productService.findByIds(ids);
        }catch (Exception exception){
            log.error(exception.getMessage());
            return new ResponseEntity(Constants.ERROR_GET_MESSAGE,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
