package com.musa3team.devout.domain.order.entity;

import com.musa3team.devout.common.entity.BaseEntity;
import com.musa3team.devout.domain.member.entity.Member;
import com.musa3team.devout.domain.order.enums.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
public class Orders extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private Long storeId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "buy_member_id")
    private Member member;

    public Orders(Long storeId, Member member) {
        this.storeId = storeId;
        this.member = member;
    }

    public void updateStatus(OrderStatus status) {
        this.status = status;
    }

    public boolean changeStatus(OrderStatus orderStatus){
        if(this.status == OrderStatus.PENDING){
            return Arrays.asList(OrderStatus.CANCELED, OrderStatus.PREPARING).contains(orderStatus);
        }

        if(this.status == OrderStatus.PREPARING){
            return Objects.equals(OrderStatus.DELIVERING, orderStatus);
        }

        if(this.status == OrderStatus.DELIVERING){
            return Objects.equals(OrderStatus.DELIVERED, orderStatus);
        }

        return false;
    }

    public void setId(long l) {
    }
}