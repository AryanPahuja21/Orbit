package com.aryan.orbit.service;

import com.aryan.orbit.model.Order;

import java.util.List;

public interface OrderService {
    Order createOrder(Order order);
    Order getOrderById(Long id);
    List<Order> getAllOrders();
    void deleteOrder(Long id);
}
