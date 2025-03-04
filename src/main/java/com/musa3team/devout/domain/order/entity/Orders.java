package com.musa3team.devout.domain.order.entity;

import com.musa3team.devout.common.entity.BaseEntity;
import com.musa3team.devout.domain.member.entity.Member;
import com.musa3team.devout.domain.order.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor
public class Orders extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private Long storeId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "buy_member_id")
    private Member member;

    public Orders(Long storeId, OrderStatus status, Member member) {
        this.storeId = storeId;
        this.status = status;
        this.member = member;
    }

}
