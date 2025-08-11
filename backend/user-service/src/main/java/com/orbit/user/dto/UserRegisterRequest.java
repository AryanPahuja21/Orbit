package com.orbit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterRequest {
    @NotBlank(message = "name is required")
    private String fullName;

    @Email(message = "Email should be valid")
    private String email;

    @Size(min = 6, message = "Password length should be atleast 6")
    private String password;

    private String phone;
    private String role;
}