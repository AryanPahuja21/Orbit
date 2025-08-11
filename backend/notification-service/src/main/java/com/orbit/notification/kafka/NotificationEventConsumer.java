package com.orbit.notification.kafka;

import com.orbit.notification.dto.OrderEvent;
import com.orbit.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventConsumer {
    private final NotificationService notificationService; // your service

    @KafkaListener(topics = "order-events", groupId = "notification-group", containerFactory = "kafkaListenerContainerFactory")
    public void consume(OrderEvent event) {
        log.info("Received order event: {}", event);
        notificationService.handleOrderEvent(event);
    }
}
