package com.aryan.orbit.controller;

import com.aryan.orbit.dto.OrderRequest;
import com.aryan.orbit.dto.OrderResponse;
import com.aryan.orbit.dto.OrderStatusUpdateRequest;
import com.aryan.orbit.model.OrderStatus;
import com.aryan.orbit.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest requestOrder,
                                                     @RequestHeader("Authorization") String token) {
        log.info("Creating order for customerId={}", requestOrder.getCustomerId());
        log.debug("OrderRequest payload: {}", requestOrder);

        OrderResponse response = orderService.createOrder(requestOrder, token);
        log.info("Order created successfully with customer id={}", response.getCustomerId());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        log.info("Fetching order with id={}", id);
        OrderResponse order = orderService.getOrderById(id);

        if (order == null) {
            log.warn("Order not found with id={}", id);
            return ResponseEntity.notFound().build();
        }

        log.info("Order retrieved successfully with id={}", id);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/user/{customerId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByCustomer(@PathVariable String customerId) {
        log.info("Fetching orders for customerId={}", customerId);
        List<OrderResponse> orders = orderService.getOrdersByCustomerId(customerId);

        log.info("Found {} orders for customerId={}", orders.size(), customerId);
        return ResponseEntity.ok(orders);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        log.info("Deleting order with id={}", id);
        orderService.deleteOrder(id);
        log.info("Order deleted successfully with id={}", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{orderId}/status")
    public ResponseEntity<OrderStatus> getStatus(@PathVariable Long orderId) {
        log.info("Fetching status for orderId={}", orderId);
        OrderResponse order = orderService.getOrderById(orderId);

        if (order == null) {
            log.warn("Order not found for status check, orderId={}", orderId);
            return ResponseEntity.notFound().build();
        }

        log.info("Order status for orderId={} is {}", orderId, order.getStatus());
        return ResponseEntity.ok(OrderStatus.valueOf(order.getStatus()));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long orderId,
                                             @RequestBody OrderStatusUpdateRequest newStatus) {
        log.info("Updating status for orderId={} to {}", orderId, newStatus.getStatus());
        OrderResponse updated = orderService.updateStatus(orderId, newStatus);

        if (updated == null) {
            log.warn("Order not found while updating status, orderId={}", orderId);
            return ResponseEntity.notFound().build();
        }

        log.info("Order status updated successfully for orderId={}", orderId);
        return ResponseEntity.ok().build();
    }
}
