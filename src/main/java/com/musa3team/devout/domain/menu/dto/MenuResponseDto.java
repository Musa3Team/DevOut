package com.musa3team.devout.domain.menu.dto;

import com.musa3team.devout.domain.menu.entity.MenuCategory;
import com.musa3team.devout.domain.menu.entity.MenuStatus;
import lombok.Getter;

@Getter
public class MenuResponseDto {

    private final Long id;
    private final String name;
    private final int price;
    private final String contents;
    private final MenuStatus status;
    private final MenuCategory category;

    public MenuResponseDto(Long id, String name, int price, String contents, MenuStatus status, MenuCategory category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.contents = contents;
        this.status = status;
        this.category = category;
    }
}
