package com.musa3team.devout.domain.menu.dto;

import com.musa3team.devout.domain.menu.entity.MenuCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateMenuRequestDto {

    @Schema(description = "메뉴명", example = "메뉴명수정")
    @NotBlank(message = "메뉴 이름은 필수 입력 값입니다.")
    private String name;

    @Schema(description = "가격", example = "12000")
    @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
    private int price;

    @Schema(description = "메뉴내용", example = "new메뉴설명")
    @NotBlank(message = "메뉴 설명은 필수 입력 값입니다.")
    private String contents;

    @Schema(description = "메뉴내용", example = "DRINKS")
    @NotNull(message = "카테고리는 필수 입력 값입니다.")
    private MenuCategory category;

}
