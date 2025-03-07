package com.musa3team.devout.domain.review.dto.request;

import lombok.Getter;

@Getter
public class CreateReviewRequestDto {

    private final String contents;
    private final Integer rating;

    public CreateReviewRequestDto(String contents, Integer rating) {
        this.contents = contents;
        this.rating = rating;
    }
}
