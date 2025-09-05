package com.aryan.orbit.dto;

import com.aryan.orbit.model.OrderStatus;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusUpdateRequest implements Serializable {
    private OrderStatus status;
}
