package com.aryan.orbit.service;

import com.aryan.orbit.model.Order;
import com.aryan.orbit.model.OrderStatus;

import java.util.List;

public interface OrderService {
    Order createOrder(Order order);
    Order getOrderById(Long id);
    List<Order> getOrdersByCustomerId(String customerId);
    List<Order> getAllOrders();
    void deleteOrder(Long id);
    boolean updateStatus(Long orderId, OrderStatus newStatus);
}
