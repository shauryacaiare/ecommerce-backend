package com.ecommerce.backend.controller;


import com.ecommerce.backend.dto.requestdto.AddOrderItemReqDto;
import com.ecommerce.backend.dto.responsedto.OrderResponseDto;
import com.ecommerce.backend.service.CustomerService;
import com.ecommerce.backend.service.utility.Utility;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    private final Utility utility;

    @PostMapping("/order-item")
    public ResponseEntity<OrderResponseDto> addOrderItem(@Valid @RequestBody AddOrderItemReqDto addOrderItemReqDto){
        log.info("add order item {}",addOrderItemReqDto.getProductId());
        String loggedInCustomer = utility.getLoggedInUserEmail();
        return new ResponseEntity<>(customerService.addOrderItemToOrder(addOrderItemReqDto,loggedInCustomer), HttpStatus.CREATED);
    }

    @PostMapping("/order-place")
    public ResponseEntity<OrderResponseDto> placeOrder(@PathVariable Long orderId){
        log.info("change status of order {} to placed",orderId);
        String loggedInCustomer = utility.getLoggedInUserEmail();
        return new ResponseEntity<>(customerService.placeOrder(orderId,loggedInCustomer),HttpStatus.CREATED);
    }

    @GetMapping("/orders")
    public ResponseEntity<Set<OrderResponseDto>> getOrderHistory(){
        String loggedInCustomer = utility.getLoggedInUserEmail();
        log.info("fetch order history for user : {}",loggedInCustomer);
        return new ResponseEntity<>(customerService.getOrderHistoryForCustomer(loggedInCustomer),HttpStatus.OK);
    }
}
