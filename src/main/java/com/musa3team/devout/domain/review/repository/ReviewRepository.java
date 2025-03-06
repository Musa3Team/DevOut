package com.musa3team.devout.domain.review.repository;

import com.musa3team.devout.domain.review.dto.ReviewCountDto;
import com.musa3team.devout.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select new com.musa3team.devout.domain.review.dto.ReviewCountDto(c.order.id, count(c)) " +
            "from Review c " +
            "where c.order.id in :orderIds " +
            "group by c.order.id")
    List<ReviewCountDto> countByOrderIds(List<Long> orderIds);

    List<Review> findByOrderId(Long orderId);

//    List<Review> findByStoreIdAndRatingGreaterThanAndRatingLessOrderByCreatedAtDesc(Long storeId, int minRating, int maxRating);

}
