package com.musa3team.devout.domain.order.repository;

import com.musa3team.devout.domain.order.entity.Orders;
import com.musa3team.devout.domain.order.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    Optional<Orders> findByIdAndStatus(Long id, OrderStatus status);
}
