package com.orbit.notification.service;

import com.orbit.notification.dto.NotificationRequest;
import com.orbit.notification.dto.OrderEvent;
import com.orbit.notification.model.Notification;

import java.util.List;

public interface NotificationService {
    void sendNotification(NotificationRequest request);
    List<Notification> getUserNotifications(String userId);
    void handleOrderEvent(OrderEvent event);
}
