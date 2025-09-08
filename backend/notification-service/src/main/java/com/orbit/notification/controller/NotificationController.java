package com.orbit.notification.controller;

import com.orbit.notification.dto.NotificationRequest;
import com.orbit.notification.dto.NotificationResponse;
import com.orbit.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<Void> createNotification(@RequestBody NotificationRequest request) {
        log.info("Sending notification to userId={}", request.getUserId());
        log.debug("NotificationRequest payload: {}", request);

        notificationService.sendNotification(request);

        log.info("Notification sent successfully to userId={}", request.getUserId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationResponse>> getUserNotifications(@PathVariable String userId) {
        log.info("Fetching notifications for userId={}", userId);

        List<NotificationResponse> notifications = notificationService.getUserNotifications(userId);

        if (notifications.isEmpty()) {
            log.warn("No notifications found for userId={}", userId);
        } else {
            log.info("Found {} notifications for userId={}", notifications.size(), userId);
            log.debug("Notifications details: {}", notifications);
        }

        return ResponseEntity.ok(notifications);
    }
}
