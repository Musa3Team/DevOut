package com.musa3team.devout.domain.store.dto;

import com.musa3team.devout.common.constants.StoreCategory;
import com.musa3team.devout.domain.store.valid.ValidTenMinuteInterval;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class StoreRequestDto {
    @Schema(description = "전화번호", example = "02-1234-1234")
    @Pattern(regexp = "^(02|0[3-6][1-5])-?\\d{3,4}-?\\d{4}$", message = "잘못된 전화번호 형식입니다.")
    private String telephone_number;

    @Schema(description = "주소", example = "주소123-1234")
    private String address;

    @Schema(description = "가게설명", example = "가게설명")
    private String contents;

    @Schema(description = "가게이름", example = "가게이름")
    private String name;

    @Schema(description = "open_time", example = "8:00")
    @ValidTenMinuteInterval
    private LocalTime open_time;

    @Schema(description = "open_close", example = "21:00")
    @ValidTenMinuteInterval
    private LocalTime close_time;
  
    @Schema(description = "최소주문금액", example = "10000")
    private int minimum_price;

    @Schema(description = "카테고리", example = "CHICKEN")
    private StoreCategory category;
}
