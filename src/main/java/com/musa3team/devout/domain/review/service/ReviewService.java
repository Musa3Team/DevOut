package com.musa3team.devout.domain.review.service;

import com.musa3team.devout.domain.member.entity.Member;
import com.musa3team.devout.domain.member.repository.MemberRepository;
import com.musa3team.devout.domain.order.enums.OrderStatus;
import com.musa3team.devout.domain.order.repository.OrderItemRepository;
import com.musa3team.devout.domain.order.repository.OrderRepository;
import com.musa3team.devout.domain.review.dto.request.CreateReviewRequestDto;
import com.musa3team.devout.domain.review.dto.request.UpdateReviewRequestDto;
import com.musa3team.devout.domain.review.dto.response.ReviewResponseDto;
import com.musa3team.devout.domain.review.entity.Review;
import com.musa3team.devout.domain.order.entity.Orders;
import com.musa3team.devout.domain.review.repository.ReviewRepository;
import com.musa3team.devout.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ReviewResponseDto save(Long memberId, Long orderId, CreateReviewRequestDto requestDto) {

        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));

        if (!order.getStatus().equals(OrderStatus.DELIVERED)) {
            throw new IllegalArgumentException("배달 완료된 주문에 대해서만 리뷰를 작성할 수 있습니다.");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        Review review = new Review();
        review.setOrder(order);
        review.setMember(member);
        review.setContents(requestDto.getContents());
        review.setRating(requestDto.getRating());

        reviewRepository.save(review);

        return new ReviewResponseDto(review);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getReviewsByStore(Long storeId, Integer minRating, Integer maxRating) {

        if (minRating == null) {
            minRating = 1;
        }
        if (maxRating == null) {
            maxRating = 5;
        }
//        List<Review> reviews = reviewRepository.findByStoreIdAndRatingGreaterThanAndRatingLessOrderByCreatedAtDesc(storeId, minRating, maxRating);
//        return reviews.stream()
//                .map(ReviewResponseDto::new)
//                .collect(Collectors.toList());
        return null;
    }

    @Transactional
    public ReviewResponseDto updateReview(Long memberId, Long reviewId, UpdateReviewRequestDto updateDto) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다."));

        if (!review.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("리뷰 수정 권한이 없습니다.");
        }

        review.updateReview(updateDto.getContents(), updateDto.getRating());

        return new ReviewResponseDto(review);
    }

    @Transactional
    public void deleteReview(Long memberId, Long id) {

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다."));

        if (!review.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("리뷰 삭제 권한이 없습니다.");
        }

        reviewRepository.delete(review);
    }
}
