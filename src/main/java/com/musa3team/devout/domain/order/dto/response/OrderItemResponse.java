package com.musa3team.devout.domain.order.dto.response;

import lombok.Getter;

@Getter
public class OrderItemResponse {
    private String menuName;
    private int count;
    private int price;


    public OrderItemResponse(String menuName, int count, int price) {
        this.menuName = menuName;
        this.count = count;
        this.price = price;
    }
}
