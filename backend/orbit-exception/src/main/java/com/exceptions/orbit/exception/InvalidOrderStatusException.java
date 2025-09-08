package com.exceptions.orbit.exception;

public class InvalidOrderStatusException extends RuntimeException {
    public InvalidOrderStatusException(String status) {
        super("Invalid order status: " + status);
    }
}