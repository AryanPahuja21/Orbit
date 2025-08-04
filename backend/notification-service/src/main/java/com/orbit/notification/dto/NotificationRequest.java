package com.orbit.notification.dto;

import lombok.Data;

@Data
public class NotificationRequest {
    private String userId;
    private String message;
}

