package com.aryan.orbit.controller;

import com.aryan.orbit.dto.OrderRequest;
import com.aryan.orbit.dto.OrderResponse;
import com.aryan.orbit.dto.OrderStatusUpdateRequest;
import com.aryan.orbit.model.Order;
import com.aryan.orbit.model.OrderStatus;
import com.aryan.orbit.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest requestOrder,  @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(orderService.createOrder(requestOrder, token));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        OrderResponse order = orderService.getOrderById(id);
        return order != null ? ResponseEntity.ok(order) : ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{customerId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByCustomer(@PathVariable String customerId) {
        return ResponseEntity.ok(orderService.getOrdersByCustomerId(customerId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{orderId}/status")
    public ResponseEntity<OrderStatus> getStatus(@PathVariable Long orderId) {
        OrderResponse order = orderService.getOrderById(orderId);
        return order != null
                ? ResponseEntity.ok(OrderStatus.valueOf(order.getStatus()))
                : ResponseEntity.notFound().build();
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long orderId, @RequestBody OrderStatusUpdateRequest newStatus) {
        OrderResponse updated = orderService.updateStatus(orderId, newStatus);
        return updated != null ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
