package com.aryan.orbit.repository;

import com.aryan.orbit.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {
    OrderStatus findByOrderId(Long orderId);
}
