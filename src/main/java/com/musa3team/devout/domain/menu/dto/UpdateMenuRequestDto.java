package com.musa3team.devout.domain.menu.dto;

import com.musa3team.devout.domain.menu.entity.MenuCategory;
import lombok.Getter;

@Getter
public class UpdateMenuRequestDto {

    private String name;
    private int price;
    private String contents;
    private MenuCategory category;

}
