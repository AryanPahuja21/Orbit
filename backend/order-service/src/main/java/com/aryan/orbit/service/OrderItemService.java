package com.aryan.orbit.service;

import com.aryan.orbit.model.OrderItem;

import java.util.List;

public interface OrderItemService {
    List<OrderItem> getItemsByOrderId(Long orderId);
}
