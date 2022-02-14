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
            return new ResponseEntity<>(orderService.saveOrder(orderDto),HttpStatus.OK);
        }catch (Exception exception){
            log.error(exception.getMessage());
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        try {
            return new ResponseEntity<>(orderService.deleteOrder(id),HttpStatus.OK);
        }catch (Exception exception){
            log.error(exception.getMessage());
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/find/{id}")
    public ResponseEntity<?> find(@PathVariable("id") Long id){
        try {
            return new ResponseEntity<>(orderService.findOrder(id),HttpStatus.OK);
        }catch (Exception exception){
            log.error(exception.getMessage());
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/findByCustomer/{id}")
    public ResponseEntity<?> findByCustomer(@PathVariable("id") Long id){
        try {
            return new ResponseEntity<>(orderService.findOrderByCustomer(id),HttpStatus.OK);
        }catch (Exception exception){
            log.error(exception.getMessage());
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> findAll(){
        try {
            return new ResponseEntity<>(orderService.getAllOrders(),HttpStatus.OK);
        }catch (Exception exception){
            log.error(exception.getMessage());
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/update/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable("id") Long id){
        try {
            return new ResponseEntity<>(orderService.updateStatus(id),HttpStatus.OK);
        }catch (Exception exception){
            log.error(exception.getMessage());
            return new ResponseEntity(Constants.ERROR_UPDATE_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
