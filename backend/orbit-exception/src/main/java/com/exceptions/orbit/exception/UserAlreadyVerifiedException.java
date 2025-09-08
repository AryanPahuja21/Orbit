package com.exceptions.orbit.exception;

public class UserAlreadyVerifiedException extends RuntimeException {
    public UserAlreadyVerifiedException(Long id) {
        super("User with id " + id + " is already verified.");
    }
}