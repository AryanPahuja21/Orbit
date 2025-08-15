package com.orbit.notification.dto;

import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {
    private Long id;
    private String userId;
    private String message;
    private Instant timestamp;
    private LocalDateTime createdAt;
}
