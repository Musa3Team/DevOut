package com.musa3team.devout.domain.store.dto;

import com.musa3team.devout.common.constants.StoreCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class StoreUpdateRequestDto {
    @Schema(description = "전화번호", example = "02-1234-1234")
    private String telephone_number;

    @Schema(description = "주소", example = "new주소123-1234")
    private String address;

    @Schema(description = "가게설명", example = "new가게설명")
    private String contents;

    @Schema(description = "가게이름", example = "new가게이름")
    private String name;

    @Schema(description = "open", example = "08:00")
    private LocalTime open_time;

    @Schema(description = "close", example = "21:00")
    private LocalTime close_time;

    @Schema(description = "최소주문금액", example = "10000")
    private Long minimum_price;

    @Schema(description = "카테고리", example = "CHICKEN")
    private StoreCategory category;
}
