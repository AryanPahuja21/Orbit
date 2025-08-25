package com.aryan.orbit.service;

import com.aryan.orbit.dto.OrderEvent;
import com.aryan.orbit.kafka.OrderEventProducer;
import com.aryan.orbit.model.Order;
import com.aryan.orbit.model.OrderStatus;
import com.aryan.orbit.repository.OrderRepository;
import com.exceptions.orbit.exception.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

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

        clearCache();

        OrderEvent event = new OrderEvent(
                order.getId(),
                order.getCustomerId(),
                "ORDER_CREATED",
                OrderStatus.CREATED,
                Instant.now()
        );
        orderEventProducer.publish(event);

        return order;
    }

    @Override
    @Cacheable(value = "orders", key = "#id")
    public Order getOrderById(Long id) {
        System.out.println("Fetching order by id from db...");
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Override
    @Cacheable(value = "orders", key = "'user:' + #customerId")
    public List<Order> getOrdersByCustomerId(String customerId) {
        System.out.println("Fetching orders by customer id from db...");
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        if (orders.isEmpty()) {
            throw new OrderNotFoundException(-1L);
        }
        return orders;
    }

    @Override
    @Cacheable(value = "orders", key = "'all'")
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    @CacheEvict(value = "orders", key = "#id")
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException(id);
        }
        orderRepository.deleteById(id);
    }

    @Override
    public boolean updateStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        updateCache(order);

        OrderEvent event = new OrderEvent(
                order.getId(),
                order.getCustomerId(),
                "ORDER_STATUS_UPDATED",
                order.getStatus(),
                Instant.now()
        );
        orderEventProducer.publish(event);

        return true;
    }

    @CachePut(value = "orders", key = "#order.id")
    public Order updateCache(Order order) {
        return order;
    }

    @CacheEvict(value = "orders", allEntries = true)
    public void clearCache() {
    }
}
