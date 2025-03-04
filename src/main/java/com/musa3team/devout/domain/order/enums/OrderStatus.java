package com.musa3team.devout.domain.order.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING("대기 중", 0),
    CANCELED("주문 취소", 1),
    PREPARING("조리 중", 1),
    DELIVERING("배달 중", 2),
    DELIVERED("배달 완료", 3);

    private final String message;
    private final Integer sequence;

    OrderStatus(String message, Integer sequence) {
        this.message = message;
        this.sequence = sequence;
    }

}
