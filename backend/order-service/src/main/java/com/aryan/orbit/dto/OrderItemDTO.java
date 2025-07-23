package com.aryan.orbit.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDTO {
    private String productId;
    private int quantity;
    private double price;
}
