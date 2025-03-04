package com.musa3team.devout.domain.order.repository;

import com.musa3team.devout.domain.order.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {
}
