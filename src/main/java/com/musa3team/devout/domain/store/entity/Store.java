package com.musa3team.devout.domain.store.entity;

import com.musa3team.devout.common.entity.BaseEntity;
import com.musa3team.devout.common.constants.StoreStatus;
import com.musa3team.devout.common.constants.StoreCategory;
import com.musa3team.devout.domain.menu.entity.Menu;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Setter
public class Store extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String telephoneNumber;

    private String address;

    private String contents;

    private String name;

    private LocalTime openTime;

    private LocalTime closeTime;

    @Enumerated(EnumType.STRING)
    private StoreStatus status;

    private Long minimumPrice;

    @Enumerated(EnumType.STRING)
    private StoreCategory category;

    @OneToMany(mappedBy = "store")
    private List<Menu> menus = new ArrayList<>();

    public Store(String telephoneNumber, String address, String contents, String name, LocalTime openTime, LocalTime closeTime, Long minimumPrice, StoreCategory category) {
        this.telephoneNumber = telephoneNumber;
        this.address = address;
        this.contents = contents;
        this.name = name;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.minimumPrice = minimumPrice;
        this.category = category;
    }
}
