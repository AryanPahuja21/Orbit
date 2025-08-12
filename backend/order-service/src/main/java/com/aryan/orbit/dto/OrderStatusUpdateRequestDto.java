package com.aryan.orbit.dto;

import com.aryan.orbit.model.OrderStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusUpdateRequestDto {
    private OrderStatus status;
}
