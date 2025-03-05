package com.musa3team.devout.domain.menu.dto;

import com.musa3team.devout.domain.menu.entity.MenuCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateMenuRequestDto {

    @NotBlank(message = "메뉴 이름은 필수 입력 값입니다.")
    private String name;

    @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
    private int price;

    @NotBlank(message = "메뉴 설명은 필수 입력 값입니다.")
    private String contents;

    @NotNull(message = "카테고리는 필수 입력 값입니다.")
    private MenuCategory category;

}
