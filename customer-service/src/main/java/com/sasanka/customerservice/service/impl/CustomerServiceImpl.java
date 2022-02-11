package com.sasanka.customerservice.service.impl;

import com.sasanka.customerservice.dto.CustomerDto;
import com.sasanka.customerservice.entity.Customer;
import com.sasanka.customerservice.repository.CustomerRepository;
import com.sasanka.customerservice.service.CustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public ResponseEntity<?> saveCustomer(CustomerDto customerDto) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDto,customer);
        Customer savedCustomer = customerRepository.save(customer);
        return new ResponseEntity<>(savedCustomer, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateCustomer(CustomerDto customerDto, Long id) {
        Customer byId = customerRepository.getById(id);
        BeanUtils.copyProperties(customerDto,byId);
        byId.setId(id);
        Customer saved = customerRepository.save(byId);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteCustomer(Long id) {
        Customer byId = customerRepository.getById(id);
        customerRepository.delete(byId);
        return new ResponseEntity<>("Deleted",HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllCustomers() {
        List<Customer> all = customerRepository.findAll();
        return new ResponseEntity<>(all,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> findById(Long id) {
        Optional<Customer> byId = customerRepository.findById(id);
        return new ResponseEntity<>(byId.isPresent() ? byId : "Nothing to show",HttpStatus.OK);
    }
}
