package com.musa3team.devout.domain.review.controller;

import com.musa3team.devout.domain.review.dto.CreateReviewRequestDto;
import com.musa3team.devout.domain.review.dto.ReviewResponseDto;
import com.musa3team.devout.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/orders/{orderId}/reviews")
    public ResponseEntity<ReviewResponseDto> save(
            @SessionAttribute
            @PathVariable Long orderId,
            @RequestBody CreateReviewRequestDto requestDto) {

        ReviewResponseDto reviewResponseDto = reviewService.save(requestDto.getContents(), requestDto.getRating());
    }
}
