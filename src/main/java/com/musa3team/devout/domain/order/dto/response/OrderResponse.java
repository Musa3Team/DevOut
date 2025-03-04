package com.musa3team.devout.domain.order.dto.response;

import com.musa3team.devout.domain.order.enums.OrderStatus;
import com.musa3team.devout.domain.order.service.OrderService;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class OrderResponse {
    private Long id;
    private Long storeId;
    private int totalCount;
    private int totalPrice;
    private OrderStatus status;
    private OrderItemResponse orderItem ;

    public OrderResponse(Long id, Long storeId, int totalCount, int totalPrice, OrderStatus status, OrderItemResponse orderItem) {
        this.id = id;
        this.storeId = storeId;
        this.totalCount = totalCount;
        this.totalPrice = totalPrice;
        this.status = status;
        this.orderItem = orderItem;
    }
}
