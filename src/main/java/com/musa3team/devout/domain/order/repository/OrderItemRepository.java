package com.musa3team.devout.domain.order.repository;

import com.musa3team.devout.domain.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findByOrderId(Long orderId);
    List<OrderItem> findByOrderIdIn(List<Long> orderIds);
}
