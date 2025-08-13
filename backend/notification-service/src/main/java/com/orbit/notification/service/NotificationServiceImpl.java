package com.orbit.notification.service;

import com.orbit.notification.dto.NotificationRequest;
import com.orbit.notification.dto.NotificationResponse;
import com.orbit.notification.dto.OrderEvent;
import com.orbit.notification.mapper.NotificationMapper;
import com.orbit.notification.model.Notification;
import com.orbit.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public void sendNotification(NotificationRequest request) {
        // Convert DTO → Entity
        Notification notification = NotificationMapper.toEntity(request);
        notificationRepository.save(notification);
        log.info("Notification saved for user {}: {}", notification.getUserId(), notification.getMessage());
    }

    @Override
    public List<NotificationResponse> getUserNotifications(String userId) {
        // Fetch entities and convert → DTOs
        return notificationRepository.findByUserId(userId).stream()
                .map(NotificationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void handleOrderEvent(OrderEvent event) {
        try {
            // Convert OrderEvent → Notification entity
            Notification notification = Notification.builder()
                    .userId(event.getCustomerId())
                    .message("Your order #" + event.getOrderId() + " has been placed successfully.")
                    .timestamp(System.currentTimeMillis())
                    .createdAt(java.time.LocalDateTime.now())
                    .build();

            // Save in DB
            notificationRepository.save(notification);
            log.info("Notification saved for user {}: {}", notification.getUserId(), notification.getMessage());

        } catch (Exception e) {
            log.error("Error handling order event: {}", event, e);
            throw e; // Let Kafka's error handler manage retries/DLT
        }
    }
}
