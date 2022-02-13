package com.sasanka.orderservice.service.impl;

import com.sasanka.orderservice.dto.*;
import com.sasanka.orderservice.entity.Order;
import com.sasanka.orderservice.entity.OrderDetail;
import com.sasanka.orderservice.repository.OrderDetailRepository;
import com.sasanka.orderservice.repository.OrderRepository;
import com.sasanka.orderservice.service.OrderService;
import com.sasanka.orderservice.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED)
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${system.customer.rest.base.url}")
    private String CUSTOMER_BASE_URL;
    @Value("${system.product.rest.base.url}")
    private String PRODUCT_BASE_URL;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public ResponseEntity<?> saveOrder(OrderDto orderDto) {
        Order order = new Order();
        BeanUtils.copyProperties(orderDto,order);
        ZonedDateTime dateTime = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC);
        order.setDateTime(dateTime);
        Order saved = orderRepository.save(order);
        orderDto.setOrderId(saved.getOrderId());
        List<OrderDetailDto> orderDetails = new ArrayList<>();

        for (OrderDetailDto orderDetailDto:orderDto.getOrderDetailList()) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(orderDetailDto,orderDetail);
            orderDetail.setOrderId(saved.getOrderId());
            OrderDetail savedOrderDetail = orderDetailRepository.save(orderDetail);
            BeanUtils.copyProperties(savedOrderDetail,orderDetailDto);
            orderDetails.add(orderDetailDto);
        }

        orderDto.setOrderDetailList(orderDetails);
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> findOrder(Long id) {
        try{
            Order byId = orderRepository.getById(id);
            List<OrderDetail> byOrderId = orderDetailRepository.findByOrderId(byId.getOrderId());
            List<Long> productIdList = getProductIdList(byOrderId);

            ProductDto[] productDtos = restTemplate.postForObject(PRODUCT_BASE_URL.concat("/findAll"), productIdList,
                    ProductDto[].class);
            CustomerDto forCustomer = restTemplate.getForObject(CUSTOMER_BASE_URL.concat("/find/") + byId.getCustomerId(),
                    CustomerDto.class);

            List<ProductDto> productDtoList = null;
            if(null!=productDtos){
                productDtoList = Arrays.asList(productDtos);
            }
            CommonDto commonDto = setOrderData(byId, getOrderDetailDtoList(byOrderId,productDtoList), forCustomer);

            return new ResponseEntity<>(commonDto,HttpStatus.OK);

        }catch (Exception exception){
            log.error(exception.getMessage());
            return new ResponseEntity<>(Constants.ERROR_MESSAGE,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> findOrderByCustomer(Long id) {
        try {
            List<CommonDto> commonDtos = new ArrayList<>();
            CustomerDto customerDto = restTemplate.getForObject(CUSTOMER_BASE_URL.concat("/find/") + id,
                    CustomerDto.class);
            List<Order> byCustomerId = orderRepository.findByCustomerId(id);
            for (Order order:byCustomerId) {
                List<OrderDetail> byOrderId = orderDetailRepository.findByOrderId(order.getOrderId());
                List<Long> productIdList = getProductIdList(byOrderId);
                ProductDto[] productDtos = restTemplate.postForObject(PRODUCT_BASE_URL.concat("/findAll"), productIdList,
                        ProductDto[].class);

                List<ProductDto> productDtoList = null;
                if(null!=productDtos){
                    productDtoList = Arrays.asList(productDtos);
                }

                CommonDto commonDto = setOrderData(order, getOrderDetailDtoList(byOrderId,productDtoList), customerDto);
                commonDtos.add(commonDto);
            }

            return new ResponseEntity<>(commonDtos,HttpStatus.OK);

        }catch (Exception exception){
            log.error(exception.getMessage());
            return new ResponseEntity<>(Constants.ERROR_MESSAGE,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> deleteOrder(Long id) {
        Order byId = orderRepository.getById(id);
        orderRepository.delete(byId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    List<Long> getProductIdList(List<OrderDetail> orderDetailList){
        List<Long> ids = new ArrayList<>();
        for (OrderDetail orderDetail:orderDetailList) {
            ids.add(orderDetail.getProductId());
        }
        return ids;
    }

    List<OrderDetailDto> getOrderDetailDtoList(List<OrderDetail> orderDetailList,List<ProductDto> productDtoList){
        List<OrderDetailDto> orderDetailDtos = new ArrayList<>();
        for (OrderDetail orderDetail:orderDetailList) {
            OrderDetailDto orderDetailDto = new OrderDetailDto();
            BeanUtils.copyProperties(orderDetail,orderDetailDto);
            for(ProductDto productDto : productDtoList) {
                if(orderDetail.getProductId().equals(productDto.getId())){
                    BeanUtils.copyProperties(productDto,orderDetailDto);
                    orderDetailDto.setId(orderDetail.getId());
                }
            }
            orderDetailDtos.add(orderDetailDto);
        }
        return orderDetailDtos;
    }

    CommonDto setOrderData(Order order,List<OrderDetailDto> orderDetailDtos,CustomerDto customerDto){
        OrderDto orderDto = new OrderDto();
        BeanUtils.copyProperties(order,orderDto);
        orderDto.setOrderDetailList(orderDetailDtos);

        CommonDto commonDto = new CommonDto();
        commonDto.setOrder(orderDto);
        commonDto.setCustomer(customerDto);
        return commonDto;
    }
}
