package com.aryan.orbit.service;

import com.aryan.orbit.model.Order;
import com.aryan.orbit.model.OrderStatus;
import com.aryan.orbit.repository.OrderRepository;
import com.aryan.orbit.repository.OrderStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderStatusServiceImpl implements OrderStatusService {

    private final OrderStatusRepository orderStatusRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderStatusServiceImpl(OrderStatusRepository orderStatusRepository, OrderRepository orderRepository) {
        this.orderStatusRepository = orderStatusRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderStatus getStatusByOrderId(Long orderId) {
        return orderStatusRepository.findByOrderId(orderId);
    }

    @Override
    public OrderStatus updateStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) return null;

        OrderStatus status = orderStatusRepository.findByOrderId(orderId);
        if (status == null) {
            status = new OrderStatus();
            status.setOrder(order);
        }
        status.setStatus(newStatus);
        return orderStatusRepository.save(status);
    }
}
