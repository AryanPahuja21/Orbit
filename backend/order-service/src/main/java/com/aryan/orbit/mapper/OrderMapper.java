package com.aryan.orbit.mapper;

import com.aryan.orbit.dto.OrderItemResponse;
import com.aryan.orbit.dto.OrderRequest;
import com.aryan.orbit.dto.OrderResponse;
import com.aryan.orbit.dto.OrderStatusUpdateRequest;
import com.aryan.orbit.model.Order;
import com.aryan.orbit.model.OrderItem;
import com.aryan.orbit.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static Order toEntity(OrderRequest dto) {
        Order order = Order.builder()
                .customerId(dto.getCustomerId())
                .status(OrderStatus.CREATED)
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

    public static OrderResponse toResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setCustomerId(order.getCustomerId());
        response.setStatus(order.getStatus().name());
        response.setItems(
                order.getItems().stream()
                        .map(OrderMapper::toItemResponse)
                        .collect(Collectors.toList())
        );
        return response;
    }

    private static OrderItemResponse toItemResponse(OrderItem item) {
        OrderItemResponse response = new OrderItemResponse();
        response.setProductId(item.getProductId());
        response.setQuantity(item.getQuantity());
        response.setPrice(item.getPrice());
        return response;
    }
}
