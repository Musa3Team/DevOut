package com.musa3team.devout.domain.review.entity;

import com.musa3team.devout.common.entity.BaseEntity;
import com.musa3team.devout.domain.member.entity.Member;
import com.musa3team.devout.domain.order.entity.Orders;
import com.musa3team.devout.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "review")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false, columnDefinition = "longtext")
    private String contents;

    @Column(nullable = false)
    private Integer rating;

    public Review() {
    }

    public Review(Orders order, Member member, String contents, Integer rating) {
        this.order = order;
        this.member = member;
        this.contents = contents;
        this.rating = rating;
    }
}
