package com.orbit.notification.service;

import com.orbit.notification.dto.NotificationRequest;
import com.orbit.notification.dto.NotificationResponse;
import com.orbit.notification.dto.OrderEvent;
import com.orbit.notification.mapper.NotificationMapper;
import com.orbit.notification.model.Notification;
import com.orbit.notification.model.OrderStatus;
import com.orbit.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
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
            String messageTemplate = "";
            if(event.getEventType().equals("ORDER_CREATED")) {
                messageTemplate = "New order placed with order ID: " + event.getOrderId();
            }
            else if(event.getEventType().equals("ORDER_STATUS_UPDATED")) {
                messageTemplate = "Order status updated for order ID: " + event.getOrderId() + " to " + event.getStatus() + ".";
            }

            Notification notification = Notification.builder()
                    .userId(event.getCustomerId())
                    .message(messageTemplate)
                    .status(OrderStatus.valueOf(event.getStatus()))
                    .timestamp(Instant.now())
                    .createdAt(LocalDateTime.now())
                    .build();

            // Save in DB
            notificationRepository.save(notification);
            log.info("Notification saved for user {}: {}", notification.getUserId(), notification.getMessage());

        } catch (Exception e) {
            log.error("Error handling order event: {}", event, e);
            throw e;
        }
    }
}
