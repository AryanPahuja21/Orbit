package com.exceptions.orbit.exception;

public class OrderItemNotFoundException extends RuntimeException {
    public OrderItemNotFoundException(Long orderId) {
        super("No items found for order with id: " + orderId);
    }
}
