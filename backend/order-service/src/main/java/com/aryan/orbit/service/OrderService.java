package com.aryan.orbit.service;

import com.aryan.orbit.dto.OrderRequest;
import com.aryan.orbit.dto.OrderResponse;
import com.aryan.orbit.dto.OrderStatusUpdateRequest;
import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest order, String token);
    OrderResponse getOrderById(Long id);
    List<OrderResponse> getOrdersByCustomerId(String customerId);
    void deleteOrder(Long id);
    OrderResponse updateStatus(Long orderId, OrderStatusUpdateRequest newStatus);
}
