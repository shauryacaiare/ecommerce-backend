package com.ecommerce.backend.repository;

import com.ecommerce.backend.entity.Order;
import com.ecommerce.backend.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepo extends JpaRepository<Order, Long> {


    //N+1 PROBLEM
    Optional<Order> findByCustomer_IdAndOrderStatus(Long id, OrderStatus draft);

    @Query("SELECT o FROM order o "+
    "JOIN FETCH order_item oi "+
    "JOIN FETCH product p "+
    "WHERE o.id = :orderId")
    Optional<Order> findOrderWithItemsAndProducts(@Param("orderId") Long id);

    @Query("SELECT o FROM order o "+
    "JOIN FETCH order_item oi "+
    "JOIN FETCH product p "+
    "where o.customer_id  = :customerId")
    List<Order> findOrderByCustomerWithItemsAndProducts(@Param("customerId") Long customerId);
}
