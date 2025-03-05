package com.musa3team.devout.domain.review.dto.request;

import lombok.Getter;

@Getter
public class UpdateReviewRequestDto {

    private final String contents;
    private final Integer rating;

    public UpdateReviewRequestDto(String contents, Integer rating) {
        this.contents = contents;
        this.rating = rating;
    }
}
