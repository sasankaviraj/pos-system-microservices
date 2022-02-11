package com.sasanka.customerservice.service;

import com.sasanka.customerservice.dto.CustomerDto;
import org.springframework.http.ResponseEntity;

public interface CustomerService {

    ResponseEntity<?> saveCustomer(CustomerDto customerDto);

    ResponseEntity<?> updateCustomer(CustomerDto customerDto, Long id);

    ResponseEntity<?> deleteCustomer(Long id);

    ResponseEntity<?> getAllCustomers();

    ResponseEntity<?> findById(Long id);
}
