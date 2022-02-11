package com.sasanka.gatewayservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallBackController {

    @GetMapping("/customerFallBack")
    public ResponseEntity customerFallBackMethod(){
        return new ResponseEntity("Customer Service is taking longer than expected. please try again later", HttpStatus.REQUEST_TIMEOUT);
    }

    @GetMapping("/productFallBack")
    public ResponseEntity productFallBackMethod(){
        return new ResponseEntity("Product Service is taking longer than expected. please try again later", HttpStatus.REQUEST_TIMEOUT);
    }

    @GetMapping("/orderFallBack")
    public ResponseEntity orderFallBackMethod(){
        return new ResponseEntity("Order Service is taking longer than expected. please try again later", HttpStatus.REQUEST_TIMEOUT);
    }
}
