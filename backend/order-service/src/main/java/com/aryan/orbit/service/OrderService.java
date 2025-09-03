package com.aryan.orbit.service;

import com.aryan.orbit.dto.OrderRequest;
import com.aryan.orbit.model.Order;
import com.aryan.orbit.model.OrderStatus;

import java.util.List;

public interface OrderService {
    Order createOrder(OrderRequest order, String token);
    Order getOrderById(Long id);
    List<Order> getOrdersByCustomerId(String customerId);
    void deleteOrder(Long id);
    Order updateStatus(Long orderId, OrderStatus newStatus);
}
