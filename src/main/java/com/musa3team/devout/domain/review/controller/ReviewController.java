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
}