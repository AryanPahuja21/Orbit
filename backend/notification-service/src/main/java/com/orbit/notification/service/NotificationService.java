package com.orbit.notification.service;

import com.orbit.notification.dto.NotificationRequest;
import com.orbit.notification.dto.NotificationResponse;
import com.orbit.notification.dto.OrderEvent;

import java.util.List;

public interface NotificationService {
    void sendNotification(NotificationRequest request);
    List<NotificationResponse> getUserNotifications(String userId);
    void handleOrderEvent(OrderEvent event);
}
