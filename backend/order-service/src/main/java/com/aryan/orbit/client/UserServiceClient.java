package com.aryan.orbit.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient (name = "user-service")
public interface UserServiceClient {

    @GetMapping("/api/users/validate")
    ResponseEntity<Boolean> validateUser(
            @RequestHeader("Authorization") String token, String userId);
}
