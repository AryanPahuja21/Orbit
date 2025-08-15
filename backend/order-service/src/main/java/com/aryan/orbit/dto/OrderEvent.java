package com.aryan.orbit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class OrderEvent {
    private Long orderId;
    private String customerId;
    private String eventType;
    private String status;
    private Instant timestamp;

}
