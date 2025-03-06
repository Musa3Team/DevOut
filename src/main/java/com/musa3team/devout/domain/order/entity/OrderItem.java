package com.musa3team.devout.domain.order.entity;

import com.musa3team.devout.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "order_item")
@NoArgsConstructor
public class OrderItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    @NotBlank
    private String menuName;

    @NotNull
    private int count;

    @NotNull
    private int price;

    public OrderItem(String menuName, int count, int price, Orders order) {
        this.menuName = menuName;
        this.count = count;
        this.price = price;
        this.order = order;
    }
}
