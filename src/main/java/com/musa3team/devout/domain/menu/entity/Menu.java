package com.musa3team.devout.domain.menu.entity;

import com.musa3team.devout.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "menu")
public class Menu extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private Long storeId;
    private String name;
    private int price;
    private String contents;
    private String status;
    private String category;
}
