package com.musa3team.devout.domain.review.dto;

import lombok.Getter;

@Getter
public class ReviewCountDto {

    private final Long orderId;

    private final Long count;

    public ReviewCountDto(Long orderId, Long count) {
        this.orderId = orderId;
        this.count = count;
    }
}
