package com.aryan.orbit.service;

import com.aryan.orbit.model.OrderItem;
import com.aryan.orbit.repository.OrderItemRepository;
import com.exceptions.orbit.exception.OrderItemNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderItemServiceImpl(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public List<OrderItem> getItemsByOrderId(Long orderId) {
        log.info("Fetching order items for orderId={}", orderId);

        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);

        if (items.isEmpty()) {
            log.warn("No items found for orderId={}", orderId);
            throw new OrderItemNotFoundException(orderId);
        }

        log.info("Found {} items for orderId={}", items.size(), orderId);
        log.debug("Order items details: {}", items);

        return items;
    }
}
