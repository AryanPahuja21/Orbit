//package com.aryan.orbit.mapper;
//
//import com.aryan.orbit.model.OrderItem;
//import com.aryan.orbit.dto.OrderItemDTO;
//
//public class OrderItemMapper {
//
//    public static OrderItemDTO toDTO(OrderItem item) {
//        if (item == null) return null;
//
//        return OrderItemDTO.builder()
//                .id(item.getId())
//                .productId(item.getProductId())
//                .quantity(item.getQuantity())
//                .price(item.getPrice())
//                .build();
//    }
//
//    public static OrderItem toEntity(OrderItemDTO dto) {
//        if (dto == null) return null;
//
//        return OrderItem.builder()
//                .id(dto.getId())
//                .productId(dto.getProductId())
//                .quantity(dto.getQuantity())
//                .price(dto.getPrice())
//                .build();
//    }
//}
