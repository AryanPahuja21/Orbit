package com.aryan.orbit.dto;

import com.aryan.orbit.model.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest implements Serializable {
    private String customerId;
    private List<OrderItem> items;
}
