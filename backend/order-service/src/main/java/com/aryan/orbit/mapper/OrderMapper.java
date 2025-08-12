package com.aryan.orbit.mapper;

import com.aryan.orbit.dto.OrderRequestDto;
import com.aryan.orbit.model.Order;
import com.aryan.orbit.model.OrderItem;
import com.aryan.orbit.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static Order toEntity(OrderRequestDto dto) {
        Order order = Order.builder()
                .customerId(dto.getCustomerId())
                .status(OrderStatus.valueOf(dto.getStatus().toUpperCase()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<OrderItem> items = dto.getItems().stream()
                .map(itemDto -> OrderItem.builder()
                        .productId(itemDto.getProductId())
                        .quantity(itemDto.getQuantity())
                        .price(itemDto.getPrice())
                        .order(order)
                        .build())
                .collect(Collectors.toList());

        order.setItems(items);
        return order;
    }
}
