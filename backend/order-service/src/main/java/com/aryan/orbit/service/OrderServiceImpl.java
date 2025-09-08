package com.aryan.orbit.service;

import com.aryan.orbit.client.UserServiceClient;
import com.aryan.orbit.dto.OrderEvent;
import com.aryan.orbit.dto.OrderRequest;
import com.aryan.orbit.dto.OrderResponse;
import com.aryan.orbit.dto.OrderStatusUpdateRequest;
import com.aryan.orbit.kafka.OrderEventProducer;
import com.aryan.orbit.mapper.OrderMapper;
import com.aryan.orbit.model.Order;
import com.aryan.orbit.model.OrderStatus;
import com.aryan.orbit.repository.OrderRepository;
import com.exceptions.orbit.exception.OrderNotFoundException;
import lombok.extern.slf4j.Slf4j;
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
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderEventProducer orderEventProducer;
    private final UserServiceClient userServiceClient;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderEventProducer orderEventProducer,
                            UserServiceClient userServiceClient) {
        this.orderRepository = orderRepository;
        this.orderEventProducer = orderEventProducer;
        this.userServiceClient = userServiceClient;
    }

    @Override
    @Caching(
            put = { @CachePut(value = "orders", key = "#result.id") },
            evict = { @CacheEvict(value = "ordersByCustomer", key = "#request.customerId") }
    )
    public OrderResponse createOrder(OrderRequest request, String token) {
        log.info("Creating order for customerId={}", request.getCustomerId());
        log.debug("OrderRequest payload: {}", request);

        Order order = OrderMapper.toEntity(request);

        ResponseEntity<Boolean> response = userServiceClient.validateUser(token, request.getCustomerId());
        log.debug("User validation response for customerId={}: {}", request.getCustomerId(), response.getBody());

        if (Boolean.TRUE.equals(response.getBody())) {
            orderRepository.save(order);
            log.info("Order saved successfully with id={}", order.getId());
        } else {
            log.warn("User validation failed for customerId={}", request.getCustomerId());
            throw new OrderNotFoundException(-1L); // or custom exception
        }

        OrderEvent event = new OrderEvent(
                order.getId(),
                order.getCustomerId(),
                "ORDER_CREATED",
                OrderStatus.CREATED,
                Instant.now()
        );
        orderEventProducer.publish(event);
        log.info("ORDER_CREATED event published for orderId={}", order.getId());

        return OrderMapper.toResponse(order);
    }

    @Override
    @Cacheable(value = "orders", key = "#id")
    public OrderResponse getOrderById(Long id) {
        log.info("Fetching order by id={}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Order not found with id={}", id);
                    return new OrderNotFoundException(id);
                });
        log.info("Order retrieved successfully with id={}", id);
        return OrderMapper.toResponse(order);
    }

    @Override
    @Cacheable(value = "ordersByCustomer", key = "#customerId")
    public List<OrderResponse> getOrdersByCustomerId(String customerId) {
        log.info("Fetching orders for customerId={}", customerId);
        List<Order> orders = orderRepository.findByCustomerId(customerId);

        if (orders.isEmpty()) {
            log.warn("No orders found for customerId={}", customerId);
            throw new OrderNotFoundException(-1L);
        }

        log.info("Found {} orders for customerId={}", orders.size(), customerId);
        return orders.stream()
                .map(OrderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "orders", key = "#id"),
                    @CacheEvict(value = "ordersByCustomer", allEntries = true)
            }
    )
    public void deleteOrder(Long id) {
        log.info("Deleting order with id={}", id);
        if (!orderRepository.existsById(id)) {
            log.warn("Order not found for deletion, id={}", id);
            throw new OrderNotFoundException(id);
        }
        orderRepository.deleteById(id);
        log.info("Order deleted successfully with id={}", id);
    }

    @Override
    @Caching(
            put = { @CachePut(value = "orders", key = "#result.id") },
            evict = { @CacheEvict(value = "ordersByCustomer", key = "#result.customerId") }
    )
    public OrderResponse updateStatus(Long orderId, OrderStatusUpdateRequest status) {
        log.info("Updating order status for orderId={} to {}", orderId, status.getStatus());

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.warn("Order not found for status update, orderId={}", orderId);
                    return new OrderNotFoundException(orderId);
                });

        order.setStatus(status.getStatus());
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
        log.info("ORDER_STATUS_UPDATED event published for orderId={}", order.getId());

        return OrderMapper.toResponse(order);
    }
}
