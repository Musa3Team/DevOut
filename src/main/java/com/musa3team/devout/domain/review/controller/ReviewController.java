package com.musa3team.devout.domain.review.controller;

import com.musa3team.devout.domain.review.repository.ReviewRepository;
import com.musa3team.devout.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponseDto> save(@RequestBody CreateReviewRequestDto requestDto) {

        ReviewResponseDto reviewResponseDto = reviewService.save();
    }
}
