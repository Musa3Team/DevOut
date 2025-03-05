package com.musa3team.devout.domain.review.entity;

import com.musa3team.devout.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "review")
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    @Column(nullable = false, columnDefinition = "longtext")
    private String contents;

    @Column(nullable = false)
    private Integer rating;

    public Review() {
    }

    public Review(String contents, Integer rating) {
        this.contents = contents;
        this.rating = rating;
    }
}
