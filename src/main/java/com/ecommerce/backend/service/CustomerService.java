package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.requestdto.AddOrderItemReqDto;
import com.ecommerce.backend.dto.responsedto.OrderResponseDto;
import com.ecommerce.backend.entity.*;
import com.ecommerce.backend.excpetion.InsufficientStockException;
import com.ecommerce.backend.excpetion.InvalidOrderStateException;
import com.ecommerce.backend.excpetion.ResourceNotFoundException;
import com.ecommerce.backend.repository.CustomerRepository;
import com.ecommerce.backend.repository.OrderRepo;
import com.ecommerce.backend.repository.ProductRepo;
import com.ecommerce.backend.service.utility.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerService {

    private final ProductRepo productRepo;
    private final CustomerRepository customerRepository;
    private final OrderRepo orderRepo;

    private final Utility utility;

    @Transactional
    public OrderResponseDto addOrderItemToOrder(AddOrderItemReqDto addOrderItemReqDto,String loggedInCustomer) {
        Long productId = addOrderItemReqDto.getProductId();
        Customer customer = customerRepository.findByEmail(loggedInCustomer)
                .orElseThrow(() ->new ResourceNotFoundException("Customer","customer with Email",loggedInCustomer));
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product","productId",productId));
        if(addOrderItemReqDto.getQuantity() > product.getStock()){
            throw new ResourceNotFoundException("Product",String.format("product with quantity %s",product.getStock()),productId);
        }
        Order order = isCustomerWithOrderInDraftExisting(customer.getId())
                .orElseGet(() -> createNewOrderInDraftStatus(customer));

        Optional<OrderItem> existingOrderItem = order.getOrderItems().stream()
                .filter(orderItem -> orderItem.getProduct().getId().equals(productId))
                .findFirst();

        if(existingOrderItem.isPresent()){
            OrderItem orderItem = existingOrderItem.get();
            orderItem.setQuantity(orderItem.getQuantity() + addOrderItemReqDto.getQuantity());
            orderItem.setPrice(product.getPrice());
        }
        else{
            OrderItem newOrderItem = OrderItem.builder()
                    .product(product)
                    .price(product.getPrice())
                    .quantity(addOrderItemReqDto.getQuantity())
                    .build();
            order.addOrderItem(newOrderItem);
        }

        BigDecimal totalPrice = order.getOrderItems()
                .stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, (sum, price) -> sum.add(price));
        order.setTotalPrice(totalPrice);
        Order savedOrder = orderRepo.save(order);
        return utility.mapToOrderResponseDto(savedOrder);
    }

    @Transactional
    public OrderResponseDto placeOrder(Long orderId, String loggedInCustomer) {
        Customer customer = customerRepository.findByEmail(loggedInCustomer).orElseThrow(() -> new ResourceNotFoundException("Customer","customer with email",loggedInCustomer));
        Order order = orderRepo.findOrderWithItemsAndProducts(orderId).orElseThrow(() -> new ResourceNotFoundException("Order","id",orderId));
        if(!order.getCustomer().getId().equals(customer.getId())){
            throw new AccessDeniedException("you don't have access to this order");
        }
        if(!order.getOrderStatus().equals(OrderStatus.DRAFT)){
            throw new InvalidOrderStateException(String.format("Order is already in %s status",order.getOrderStatus().name()));
        }
        for (OrderItem item : order.getOrderItems()) {
            if (item.getProduct().getStock() < item.getQuantity()) {
                throw new InsufficientStockException(
                        item.getProduct().getName(),
                        item.getProduct().getStock(),
                        item.getQuantity()
                );
            }
        }

        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() - item.getQuantity());
            // ✅ No productRepo.save() needed!
            // @Transactional dirty checking handles it automatically
        }
        order.setOrderStatus(OrderStatus.PLACED);
        Order savedOrder = orderRepo.save(order);

        return utility.mapToOrderResponseDto(savedOrder);
    }

    @Transactional(readOnly = true)
    public Set<OrderResponseDto> getOrderHistoryForCustomer(String loggedInCustomer) {
        Customer customer = customerRepository.findByEmail(loggedInCustomer)
                .orElseThrow(() -> new ResourceNotFoundException("Customer","customer email",loggedInCustomer));
        Long customerId = customer.getId();
        List<Order> orders = orderRepo.findOrderByCustomerWithItemsAndProducts(customerId);
        return mapToOrderResponseDtoSet(orders);
    }

    private Set<OrderResponseDto> mapToOrderResponseDtoSet(List<Order> orders) {
        return orders.stream()
                .map(utility::mapToOrderResponseDto)
                .collect(Collectors.toSet());
    }

    private Order createNewOrderInDraftStatus(Customer customer) {
        return Order.builder()
                .totalPrice(BigDecimal.ZERO)
                .customer(customer)
                .orderStatus(OrderStatus.DRAFT)
                .build();
    }

    private Optional<Order> isCustomerWithOrderInDraftExisting(Long id) {
       return orderRepo.findByCustomer_IdAndOrderStatus(id, OrderStatus.DRAFT);
    }



}
