package com.musa3team.devout.domain.order.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class CreateOrderRequest {
    private Long storeId;
    private CreateOrderItemRequest orderItem;

}
