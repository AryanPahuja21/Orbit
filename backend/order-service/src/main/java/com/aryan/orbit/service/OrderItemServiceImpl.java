package com.aryan.orbit.service;

import com.aryan.orbit.model.OrderItem;
import com.aryan.orbit.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderItemServiceImpl(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public List<OrderItem> getItemsByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }
}
