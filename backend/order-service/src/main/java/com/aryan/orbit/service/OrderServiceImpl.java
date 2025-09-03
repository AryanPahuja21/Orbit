package com.aryan.orbit.service;

import com.aryan.orbit.client.UserServiceClient;
import com.aryan.orbit.dto.OrderEvent;
import com.aryan.orbit.dto.OrderRequest;
import com.aryan.orbit.kafka.OrderEventProducer;
import com.aryan.orbit.mapper.OrderMapper;
import com.aryan.orbit.model.Order;
import com.aryan.orbit.model.OrderStatus;
import com.aryan.orbit.repository.OrderRepository;
import com.exceptions.orbit.exception.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderEventProducer orderEventProducer;
    private final UserServiceClient userServiceClient;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderEventProducer orderEventProducer, UserServiceClient userServiceClient) {
        this.orderRepository = orderRepository;
        this.orderEventProducer = orderEventProducer;
        this.userServiceClient = userServiceClient;
    }

    @Override
    @Caching(
            put = { @CachePut(value = "orders", key = "#result.id") },
            evict = { @CacheEvict(value = "ordersByCustomer", key = "#request.customerId") }
    )
    public Order createOrder(OrderRequest request, String token) {
        Order order = OrderMapper.toEntity(request);
        ResponseEntity<Boolean> response = userServiceClient.validateUser(token, request.getCustomerId());

        if (Boolean.TRUE.equals(response.getBody())) {
            orderRepository.save(order);
        }

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
    @Cacheable(value = "ordersByCustomer", key = "#customerId")
    public List<Order> getOrdersByCustomerId(String customerId) {
        System.out.println("Fetching orders by customer id from db...");
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        if (orders.isEmpty()) {
            throw new OrderNotFoundException(-1L);
        }
        return orders;
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "orders", key = "#id"),
                    @CacheEvict(value = "ordersByCustomer", allEntries = true)
            }
    )
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException(id);
        }
        orderRepository.deleteById(id);
    }

    @Override
    @Caching(
            put = { @CachePut(value = "orders", key = "#result.id") },
            evict = { @CacheEvict(value = "ordersByCustomer", key = "#result.customerId") }
    )
    public Order updateStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        OrderEvent event = new OrderEvent(
                order.getId(),
                order.getCustomerId(),
                "ORDER_STATUS_UPDATED",
                order.getStatus(),
                Instant.now()
        );
        orderEventProducer.publish(event);

        return order;
    }
}
