package com.musa3team.devout.domain.review.controller;

import com.musa3team.devout.domain.order.entity.Orders;
import com.musa3team.devout.domain.order.enums.OrderStatus;
import com.musa3team.devout.domain.order.service.OrderService;
import com.musa3team.devout.domain.review.dto.request.CreateReviewRequestDto;
import com.musa3team.devout.domain.review.dto.response.ReviewResponseDto;
import com.musa3team.devout.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final OrderService orderService;

    // 리뷰 생성
    @PostMapping("/orders/{orderId}/reviews")
    public ResponseEntity<ReviewResponseDto> save(
            @SessionAttribute(name = "LOGIN_USER") Long memberId,
            @PathVariable Long orderId,
            @RequestBody CreateReviewRequestDto requestDto) {

        Orders order = orderService.findByIdAndStatus(orderId, OrderStatus.DELIVERED)
                .orElseThrow(() -> new IllegalArgumentException("배달 완료된 주문만 리뷰를 작성할 수 있습니다."));

        ReviewResponseDto reviewResponseDto = reviewService.save(memberId, orderId, requestDto);

        return ResponseEntity.ok(reviewResponseDto);
    }

    // 리뷰 조회
    @GetMapping("/stores/{storeId}/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByStore(
            @PathVariable Long storeId,
            @RequestParam(required = false) Integer minRating,
            @RequestParam(required = false) Integer maxRating) {

        List<ReviewResponseDto> reviews = reviewService.getReviewsByStore(storeId, minRating, maxRating);
        return ResponseEntity.ok(reviews);
    }
}