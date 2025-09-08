package com.orbit.user.controller;

import com.orbit.user.dto.*;
import com.orbit.user.service.UserService;
import com.orbit.user.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public String extractToken(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        log.error("Invalid Authorization header: {}", header);
        throw new RuntimeException("Invalid Authorization header");
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRegisterRequest request) {
        log.info("Received user registration request for email={}", request.getEmail());
        UserResponse response = userService.registerUser(request);
        log.info("User registered successfully with id={}", response.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody UserLoginRequest request) {
        log.info("Login attempt for email={}", request.getEmail());
        AuthResponse response = userService.loginUser(request);
        log.info("Login successful for email={}", request.getEmail());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody UserUpdateRequest request
    ) {
        log.info("Updating user with id={}", id);
        UserResponse response = userService.updateUser(id, request);
        log.info("User updated successfully with id={}", id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        log.info("Fetching user with id={}", id);
        UserResponse response = userService.getUserById(id);
        log.info("User retrieved successfully with id={}", id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        log.info("Deleting user with id={}", id);
        userService.deleteUser(id);
        log.info("User deleted successfully with id={}", id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateUser(@RequestHeader("Authorization") String token,
                                                @RequestParam("userId") String userId) {
        log.info("Validating token for userId={}", userId);
        String extracted = extractToken(token);
        boolean valid = jwtService.isTokenValid(extracted, userId);
        log.info("Validation result for userId={} : {}", userId, valid);
        return ResponseEntity.ok()
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .body(valid);
    }
}
