package com.aryan.orbit.service;

import com.aryan.orbit.model.OrderStatus;

public interface OrderStatusService {
    OrderStatus getStatusByOrderId(Long orderId);
    OrderStatus updateStatus(Long orderId, String status);
}
