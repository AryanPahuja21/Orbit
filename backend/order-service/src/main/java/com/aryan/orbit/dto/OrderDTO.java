package com.aryan.orbit.dto;


import com.aryan.orbit.model.OrderItem;
import lombok.*;

        import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    private String customerId;
    private List<OrderItemDTO> items;
}
