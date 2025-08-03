package com.aryan.orbit.mapper;

import com.aryan.orbit.model.Order;
import com.aryan.orbit.dto.OrderDTO;
import com.aryan.orbit.dto.OrderItemDTO;

import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderDTO toDTO(Order order) {
        if (order == null) return null;

        return OrderDTO.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .items(order.getItems() != null ?
                        order.getItems().stream().map(OrderItemMapper::toDTO).collect(Collectors.toList()) :
                        null)
                .build();
    }

    public static Order toEntity(OrderDTO dto) {
        if (dto == null) return null;

        return Order.builder()
                .id(dto.getId())
                .customerId(dto.getCustomerId())
                .status(dto.getStatus())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .items(dto.getItems() != null ?
                        dto.getItems().stream().map(OrderItemMapper::toEntity).collect(Collectors.toList()) :
                        null)
                .build();
    }
}
