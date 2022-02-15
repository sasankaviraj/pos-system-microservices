package com.sasanka.orderservice.service.impl;

import com.sasanka.orderservice.dto.*;
import com.sasanka.orderservice.entity.Order;
import com.sasanka.orderservice.entity.OrderDetail;
import com.sasanka.orderservice.exception.POSSystemException;
import com.sasanka.orderservice.repository.OrderDetailRepository;
import com.sasanka.orderservice.repository.OrderRepository;
import com.sasanka.orderservice.service.OrderService;
import com.sasanka.orderservice.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = POSSystemException.class)
    @Override
    public OrderDto saveOrder(OrderDto orderDto) throws POSSystemException{
        try {
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
            return orderDto;
        }catch (Exception exception){
            log.error(exception.getMessage());
            throw new POSSystemException(Constants.ERROR_POST_MESSAGE);
        }
    }

    @Override
    public CommonDto findOrder(Long id) throws POSSystemException {
        try{
            Order byId = orderRepository.getById(id);
            List<OrderDetail> byOrderId = orderDetailRepository.findByOrderId(byId.getOrderId());

            CustomerDto forCustomer = findCustomer(byId.getCustomerId());

            CommonDto commonDto = setOrderData(byId, getOrderDetailDtoList(byOrderId,getProductData(byOrderId)), forCustomer);

            return commonDto;

        }catch (Exception exception){
            log.error(exception.getMessage());
            throw new POSSystemException(Constants.ERROR_GET_MESSAGE);
        }
    }

    @Override
    public List<CommonDto> findOrderByCustomer(Long id) throws POSSystemException {
        try {
            List<CommonDto> commonDtos = new ArrayList<>();
            CustomerDto customerDto = findCustomer(id);
            List<Order> byCustomerId = orderRepository.findByCustomerId(id);
            for (Order order:byCustomerId) {
                List<OrderDetail> byOrderId = orderDetailRepository.findByOrderId(order.getOrderId());

                CommonDto commonDto = setOrderData(order, getOrderDetailDtoList(byOrderId,getProductData(byOrderId)), customerDto);
                commonDtos.add(commonDto);
            }

            return commonDtos;

        }catch (Exception exception){
            log.error(exception.getMessage());
            throw new POSSystemException(Constants.ERROR_GET_MESSAGE);
        }
    }

    @Override
    public Long deleteOrder(Long id) throws POSSystemException{
        try{
            Order byId = orderRepository.getById(id);
            orderRepository.delete(byId);
            return id;
        }catch (Exception exception){
            log.error(exception.getMessage());
            throw new POSSystemException(Constants.ERROR_DELETE_MESSAGE);
        }
    }

    @Override
    public Long updateStatus(Long id) throws POSSystemException {
        try{
            Order byId = orderRepository.getById(id);
            byId.setCompleted(true);
            orderRepository.save(byId);
            return id;
        }catch (Exception exception){
            log.error(exception.getMessage());
            throw new POSSystemException(Constants.ERROR_UPDATE_MESSAGE);
        }
    }

    @Override
    public List<List<CommonDto>> getAllOrders() throws POSSystemException {
        try {
            List<List<CommonDto>> list = new ArrayList<>();

            CustomerDto[] customerArray = restTemplate.getForObject(CUSTOMER_BASE_URL.concat("/all"),
                    CustomerDto[].class);

            if(null!=customerArray){
                for (CustomerDto customerDto: customerArray) {
                    List<CommonDto> orderByCustomer = findOrderByCustomer(customerDto.getId());
                    list.add(orderByCustomer);
                }
            }
            return list;

        }catch (Exception exception){
            log.error(exception.getMessage());
            throw new POSSystemException(Constants.ERROR_GET_MESSAGE);
        }
    }

    List<ProductDto> getProductData(List<OrderDetail> orderDetailList){
        List<Long> ids = new ArrayList<>();
        List<ProductDto> productDtoList = null;
        for (OrderDetail orderDetail:orderDetailList) {
            ids.add(orderDetail.getProductId());
        }
        ProductDto[] productDtos = restTemplate.postForObject(PRODUCT_BASE_URL.concat("/findAll"), ids,
                ProductDto[].class);
        if(null!=productDtos){
            productDtoList = Arrays.asList(productDtos);
        }
        return productDtoList;
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

    CustomerDto findCustomer(Long id){
        return restTemplate.getForObject(CUSTOMER_BASE_URL.concat("/find/") + id,
                CustomerDto.class);
    }

    CommonDto setOrderData(Order order,List<OrderDetailDto> orderDetailDtos,CustomerDto customerDto){
        OrderDto orderDto = new OrderDto();
        BeanUtils.copyProperties(order,orderDto);
        orderDto.setOrderDetailList(orderDetailDtos);
        orderDto.setDateTime(order.getDateTime().toLocalDate());
        CommonDto commonDto = new CommonDto();
        commonDto.setOrder(orderDto);
        commonDto.setCustomer(customerDto);
        return commonDto;
    }
}
