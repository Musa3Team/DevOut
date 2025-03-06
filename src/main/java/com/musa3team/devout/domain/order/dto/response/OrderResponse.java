package com.musa3team.devout.domain.order.dto.response;

import com.musa3team.devout.domain.order.entity.OrderItem;
import com.musa3team.devout.domain.order.entity.Orders;
import com.musa3team.devout.domain.order.enums.OrderStatus;
import lombok.Getter;

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

    public static OrderResponse toDto(Orders order, OrderItem orderItem, int totalPrice){
        return new OrderResponse(
                order.getId(),
                order.getStoreId(),
                orderItem.getCount(),
                totalPrice,
                order.getStatus(),
                new OrderItemResponse(orderItem.getMenuName(), orderItem.getCount(), orderItem.getPrice())
        );
    }
}
