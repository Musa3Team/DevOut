package com.musa3team.devout.domain.review.dto;

import lombok.Getter;

@Getter
public class ReviewResponseDto {

    private final Long id;

    private final Long storeId;

    private final Long orderId;

    private final Long buyMemberId;

    private final Long menuId;

    private final String contents;

    private final Integer rating;

    public ReviewResponseDto(Long id, Long storeId, Long orderId, Long buyMemberId, Long menuId, String contents, Integer rating) {
        this.id = id;
        this.storeId = storeId;
        this.orderId = orderId;
        this.buyMemberId = buyMemberId;
        this.menuId = menuId;
        this.contents = contents;
        this.rating = rating;
    }
}
