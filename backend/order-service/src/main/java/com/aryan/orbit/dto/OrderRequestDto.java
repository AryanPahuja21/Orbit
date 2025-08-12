package com.aryan.orbit.dto;

import com.aryan.orbit.model.OrderItem;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDto {
    private String customerId;
    private String status;
    private List<OrderItem> items;
}
