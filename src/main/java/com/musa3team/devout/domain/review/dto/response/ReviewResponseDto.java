package com.musa3team.devout.domain.review.dto.response;

import com.musa3team.devout.domain.review.entity.Review;
import lombok.Getter;

@Getter
public class ReviewResponseDto {

    private final Long id;
    private final Long orderId;
    private final Long memberId;
    private final String contents;
    private final Integer rating;

    public ReviewResponseDto(Review review) {
        this.id = review.getId();
        this.orderId = review.getOrder().getId();
        this.memberId = review.getMember().getId();
        this.contents = review.getContents();
        this.rating = review.getRating();
    }
}
