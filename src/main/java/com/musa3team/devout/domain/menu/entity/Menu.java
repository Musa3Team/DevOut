package com.musa3team.devout.domain.menu.entity;

import com.musa3team.devout.common.entity.BaseEntity;
import com.musa3team.devout.domain.store.entity.Store;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "menu")
public class Menu extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private String name;
    private int price;
    private String contents;
    @Enumerated(EnumType.STRING)
    private MenuStatus status;
    @Enumerated(EnumType.STRING)
    private MenuCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    public Menu(Store store, String name, int price, String contents, MenuCategory category) {
        this.store = store;
        this.name = name;
        this.price = price;
        this.contents = contents;
        this.status = MenuStatus.ACTIVE;
        this.category = category;
    }

    public void disable() {
        this.status = MenuStatus.DISABLED;
    }

    public void update(String name, int price, String contents, MenuCategory category) {
        this.name = name;
        this.price = price;
        this.contents = contents;
        this.category = category;
    }
}
