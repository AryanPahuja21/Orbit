package com.aryan.orbit.service;

import com.aryan.orbit.dto.OrderEvent;
import com.aryan.orbit.kafka.OrderEventProducer;
import com.aryan.orbit.model.Order;
import com.aryan.orbit.model.OrderStatus;
import com.aryan.orbit.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderEventProducer orderEventProducer;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderEventProducer orderEventProducer) {
        this.orderRepository = orderRepository;
        this.orderEventProducer = orderEventProducer;
    }

    @Override
    public Order createOrder(Order order) {
        orderRepository.save(order);

        OrderEvent event = new OrderEvent(order.getId(), order.getCustomerId(), "ORDER_CREATED", "CREATED", Instant.now());
        orderEventProducer.publish(event);
        return order;
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public boolean updateStatus(Long orderId, OrderStatus newStatus) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(newStatus);
            order.setUpdatedAt(LocalDateTime.now());
            orderRepository.save(order);
            return true;
        }

        return false;
    }
}
