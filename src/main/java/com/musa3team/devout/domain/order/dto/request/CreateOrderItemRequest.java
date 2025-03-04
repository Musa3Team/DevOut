package com.musa3team.devout.domain.order.dto.request;

import lombok.Getter;

@Getter
public class CreateOrderItemRequest {
    private Long menuId;
    private int count;
    private int price;
}
