package com.musa3team.devout.domain.review.controller;

import com.musa3team.devout.common.jwt.JwtUtil;
import com.musa3team.devout.domain.order.entity.Orders;
import com.musa3team.devout.domain.order.enums.OrderStatus;
import com.musa3team.devout.domain.order.service.OrderService;
import com.musa3team.devout.domain.review.dto.request.CreateReviewRequestDto;
import com.musa3team.devout.domain.review.dto.request.UpdateReviewRequestDto;
import com.musa3team.devout.domain.review.dto.response.ReviewResponseDto;
import com.musa3team.devout.domain.review.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final OrderService orderService;
    private final JwtUtil jwtUtil;

    // 리뷰 생성
    @PostMapping("/orders/{orderId}/reviews")
    public ResponseEntity<ReviewResponseDto> save(
            @PathVariable Long orderId,
            @RequestBody CreateReviewRequestDto requestDto, HttpServletRequest request
    ) {
        String token = jwtUtil.extractToken(request);
        Long memberId = jwtUtil.extractMemberId(token);

        Orders order = orderService.findByIdAndStatus(orderId, OrderStatus.DELIVERED)
                .orElseThrow(() -> new IllegalArgumentException("배달 완료된 주문만 리뷰를 작성할 수 있습니다."));

        ReviewResponseDto reviewResponseDto = reviewService.save(memberId, orderId, requestDto);

        return ResponseEntity.ok(reviewResponseDto);
    }

    // 리뷰 조회
    @GetMapping("/stores/{storeId}/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByStore(
            @PathVariable Long storeId,
            HttpServletRequest request,
            @RequestParam(required = false) Integer minRating,
            @RequestParam(required = false) Integer maxRating
    ) {
        log.info("API 호출성공");
        String token = jwtUtil.extractToken(request);
        Long memberId = jwtUtil.extractMemberId(token);

        List<ReviewResponseDto> reviews = reviewService.getReviewsByStore(storeId, minRating, maxRating);
        return ResponseEntity.ok(reviews);
    }

    // 리뷰 수정
    @PutMapping("/reviews/{id}")
    public ResponseEntity<ReviewResponseDto> updateReview(
            @PathVariable Long id,
            @RequestBody UpdateReviewRequestDto updateDto, HttpServletRequest request
    ) {
        String token = jwtUtil.extractToken(request);
        Long memberId = jwtUtil.extractMemberId(token);

        ReviewResponseDto updatedReview = reviewService.updateReview(memberId, id, updateDto);
        return ResponseEntity.ok(updatedReview);
    }

    // 리뷰 삭제
    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long id, HttpServletRequest request
    ) {
        String token = jwtUtil.extractToken(request);
        Long memberId = jwtUtil.extractMemberId(token);

        reviewService.deleteReview(memberId, id);
        return ResponseEntity.noContent().build();
    }
}