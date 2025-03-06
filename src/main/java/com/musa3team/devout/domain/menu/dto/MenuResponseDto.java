package com.musa3team.devout.domain.menu.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuResponseDto {
    private Long id;
    private String name;
    private String contents;
    private int price;

    public MenuResponseDto(Long id, String name, String contents, int price) {
        this.id = id;
        this.name = name;
        this.contents = contents;
        this.price = price;
    }
}
