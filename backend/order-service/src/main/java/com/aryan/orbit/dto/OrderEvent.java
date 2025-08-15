package com.aryan.orbit.dto;

import com.aryan.orbit.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class OrderEvent {
    private Long orderId;
    private String customerId;
    private String eventType;
    private OrderStatus status;
    private Instant timestamp;
}
