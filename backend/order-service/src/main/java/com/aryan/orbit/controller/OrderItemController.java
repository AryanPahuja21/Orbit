package com.aryan.orbit.controller;

import com.aryan.orbit.model.OrderItem;
import com.aryan.orbit.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders/{orderId}/items")
@RequiredArgsConstructor
@Slf4j
public class OrderItemController {

    private final OrderItemService orderItemService;

    @GetMapping
    public ResponseEntity<List<OrderItem>> getItemsByOrderId(@PathVariable Long orderId) {
        log.info("Fetching items for orderId={}", orderId);

        List<OrderItem> items = orderItemService.getItemsByOrderId(orderId);

        if (items.isEmpty()) {
            log.warn("No items found for orderId={}", orderId);
        } else {
            log.info("Found {} items for orderId={}", items.size(), orderId);
            log.debug("Order items: {}", items);
        }

        return ResponseEntity.ok(items);
    }
}
