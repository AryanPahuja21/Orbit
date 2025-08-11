package com.orbit.user.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {
    private String fullName;
    private String password;
    private String phone;
}