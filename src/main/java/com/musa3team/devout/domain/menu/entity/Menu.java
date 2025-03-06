package com.musa3team.devout.domain.menu.entity;

import com.musa3team.devout.common.entity.BaseEntity;
import com.musa3team.devout.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "menu")
@NoArgsConstructor
@AllArgsConstructor
public class Menu extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;
    private String name;
    private int price;
    private String contents;
    private String status;
    private String category;
}
