package com.orbit.notification.mapper;

import com.orbit.notification.dto.NotificationRequest;
import com.orbit.notification.dto.NotificationResponse;
import com.orbit.notification.model.Notification;

import java.time.Instant;

public class NotificationMapper {

    public static Notification toEntity(NotificationRequest request) {
        if (request == null) return null;

        return Notification.builder()
                .userId(request.getUserId())
                .message(request.getMessage())
                .timestamp(Instant.now())
                .createdAt(java.time.LocalDateTime.now())
                .build();
    }

    public static NotificationResponse toDto(Notification entity) {
        if (entity == null) return null;

        return NotificationResponse.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .message(entity.getMessage())
                .timestamp(entity.getTimestamp())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
