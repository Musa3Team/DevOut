package com.musa3team.devout.domain.menu.dto;

import com.musa3team.devout.domain.menu.entity.MenuCategory;
import com.musa3team.devout.domain.menu.entity.MenuStatus;
import lombok.Getter;

@Getter
public class StoreMenuResponseDto {

    private final Long id;
    private final String name;
    private final String contents;
    private final int price;

    public StoreMenuResponseDto(Long id, String name, String contents, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.contents = contents;
    }
}
