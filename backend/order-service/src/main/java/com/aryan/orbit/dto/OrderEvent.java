package com.aryan.orbit.dto;

import java.time.Instant;

public class OrderEvent {
    private Long orderId;
    private String eventType;
    private String status;
    private String customerId;
    private Instant timestamp;

}
