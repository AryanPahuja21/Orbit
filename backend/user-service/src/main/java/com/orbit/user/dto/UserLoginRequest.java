package com.orbit.user.dto;

import lombok.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginRequest {
    @Email(message = "should be a valid email")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}