package com.aryan.orbit.controller;

import com.aryan.orbit.model.OrderStatus;
import com.aryan.orbit.service.OrderStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/status")
public class OrderStatusController {

    private final OrderStatusService orderStatusService;

    @Autowired
    public OrderStatusController(OrderStatusService orderStatusService) {
        this.orderStatusService = orderStatusService;
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderStatus> getStatusByOrderId(@PathVariable Long orderId) {
        OrderStatus status = orderStatusService.getStatusByOrderId(orderId);
        return status != null ? ResponseEntity.ok(status) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderStatus> updateStatus(@PathVariable Long orderId,
                                                    @RequestParam String newStatus) {
        OrderStatus updated = orderStatusService.updateStatus(orderId, newStatus);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.badRequest().build();
    }
}
