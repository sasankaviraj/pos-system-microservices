package com.sasanka.customerservice.controller;

import com.sasanka.customerservice.dto.CustomerDto;
import com.sasanka.customerservice.service.CustomerService;
import com.sasanka.customerservice.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.BASE_URL)
@Slf4j
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody CustomerDto customerDto){
        try {
            return customerService.saveCustomer(customerDto);
        }catch (Exception exception){
            log.error(exception.getMessage());
            return new ResponseEntity(Constants.ERROR_POST_MESSAGE,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@RequestBody CustomerDto customerDto,@PathVariable("id") Long id){
        try {
            return customerService.updateCustomer(customerDto,id);
        }catch (Exception exception){
            log.error(exception.getMessage());
            return new ResponseEntity(Constants.ERROR_UPDATE_MESSAGE,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        try {
            return customerService.deleteCustomer(id);
        }catch (Exception exception){
            log.error(exception.getMessage());
            return new ResponseEntity(Constants.ERROR_DELETE_MESSAGE,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAll(){
        try {
            return customerService.getAllCustomers();
        }catch (Exception exception){
            log.error(exception.getMessage());
            return new ResponseEntity(Constants.ERROR_GET_MESSAGE,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/find/{id}")
    public ResponseEntity<?> find(@PathVariable("id") Long id){
        try {
            return customerService.findById(id);
        }catch (Exception exception){
            log.error(exception.getMessage());
            return new ResponseEntity(Constants.ERROR_GET_MESSAGE,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
