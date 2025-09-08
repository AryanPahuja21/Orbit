package com.aryan.orbit.dto;

import com.aryan.orbit.model.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusUpdateRequest implements Serializable {
    @NotNull(message = "Order status is required")
    private OrderStatus status;
}
