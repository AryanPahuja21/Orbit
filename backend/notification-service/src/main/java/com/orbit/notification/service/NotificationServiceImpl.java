package com.orbit.notification.service;

import com.orbit.notification.dto.NotificationRequest;
import com.orbit.notification.dto.OrderEvent;
import com.orbit.notification.model.Notification;
import com.orbit.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public void sendNotification(NotificationRequest request) {
        Notification notification = Notification.builder()
                .userId(request.getUserId())
                .message(request.getMessage())
                .createdAt(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getUserNotifications(String userId) {
        return notificationRepository.findByUserId(userId);
    }

    @Override
    public void handleOrderEvent(OrderEvent event) {
        try {
            // Convert OrderEvent → Notification
            Notification notification = Notification.builder()
                    .userId(event.getCustomerId())
                    .message("Your order #" + event.getOrderId() + " has been placed successfully.")
                    .timestamp(System.currentTimeMillis())
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
