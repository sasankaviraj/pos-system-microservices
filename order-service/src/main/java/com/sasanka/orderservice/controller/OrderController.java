package com.sasanka.orderservice.controller;

import com.sasanka.orderservice.dto.OrderDto;
import com.sasanka.orderservice.service.OrderService;
import com.sasanka.orderservice.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.BASE_URL)
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody OrderDto orderDto){
        try {
            return orderService.saveOrder(orderDto);
        }catch (Exception exception){
            log.error(exception.getMessage());
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/delete/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        try {
            return orderService.deleteOrder(id);
        }catch (Exception exception){
            log.error(exception.getMessage());
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/find/{id}")
    public ResponseEntity<?> find(@PathVariable("id") Long id){
        try {
            return orderService.findOrder(id);
        }catch (Exception exception){
            log.error(exception.getMessage());
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/findByCustomer/{id}")
    public ResponseEntity<?> findByCustomer(@PathVariable("id") Long id){
        try {
            return orderService.findOrderByCustomer(id);
        }catch (Exception exception){
            log.error(exception.getMessage());
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
