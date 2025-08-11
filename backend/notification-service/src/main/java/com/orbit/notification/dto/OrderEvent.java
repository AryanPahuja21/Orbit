package com.orbit.notification.dto;

import lombok.Data;

@Data
public class OrderEvent {
    private Long orderId;
    private String eventType;
    private String status;
    private String customerId;
    private Long timestamp;
}
