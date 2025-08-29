package com.aryan.orbit.dto;

import com.aryan.orbit.model.OrderItem;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private String customerId;
    private List<OrderItem> items;
}
