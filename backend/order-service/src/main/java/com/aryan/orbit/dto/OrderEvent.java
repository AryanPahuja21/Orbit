package com.aryan.orbit.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class OrderEvent {
    private Long orderId;
    private String eventType;
    private String status;
    private String customerId;
    private Instant timestamp;

}
