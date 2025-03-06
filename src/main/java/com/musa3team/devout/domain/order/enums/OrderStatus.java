package com.musa3team.devout.domain.order.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING("대기 중"),
    CANCELED("주문 취소"),
    PREPARING("조리 중"),
    DELIVERING("배달 중"),
    DELIVERED("배달 완료");

    private final String message;

    OrderStatus(String message) {
        this.message = message;
    }
}
