package com.musa3team.devout.domain.review.repository;

import com.musa3team.devout.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByStoreIdAndRatingBetweenOrderByCreatedAtDesc(Long storeId, Integer minRating, Integer maxRating);
}
