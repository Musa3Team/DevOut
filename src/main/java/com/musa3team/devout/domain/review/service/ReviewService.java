package com.musa3team.devout.domain.review.service;

import com.musa3team.devout.domain.order.repository.OrderRepository;
import com.musa3team.devout.domain.review.dto.ReviewResponseDto;
import com.musa3team.devout.domain.review.entity.Review;
import com.musa3team.devout.domain.order.entity.Orders;
import com.musa3team.devout.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;

    public ReviewResponseDto save(String contents, Integer rating) {

        Orders findOrderId = orderRepository.findOrderByOrderIdOrElseThrow(orderId);

        Review review = new Review(contents, rating);
        review.setOrderId(findOrderId);

        reviewRepository.save(review);

        return new ReviewResponseDto(review.getId(), review.getOrderId(), review.getContents(), review.getRating());
    }
}
