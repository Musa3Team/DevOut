package com.musa3team.devout.domain.review.dto;

import lombok.Getter;

@Getter
public class CreateReviewRequestDto {

    private final Long orderId;

    private final String contents;

    private final Integer rating;

    public CreateReviewRequestDto(Long orderId, String contents, Integer rating) {
        this.orderId = orderId;
        this.contents = contents;
        this.rating = rating;
    }
}
