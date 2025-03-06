package com.musa3team.devout.domain.store.dto;

import com.musa3team.devout.common.constants.StoreCategory;
import com.musa3team.devout.domain.store.valid.ValidTenMinuteInterval;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class StoreRequestDto {
    @Pattern(regexp = "^(02|0[3-6][1-5])-?\\d{3,4}-?\\d{4}$", message = "잘못된 전화번호 형식입니다.")
    private String telephone_number;
    private String address;
    private String contents;
    private String name;
    @ValidTenMinuteInterval
    private LocalTime open_time;
    @ValidTenMinuteInterval
    private LocalTime close_time;
    private int minimum_price;
    private StoreCategory category;
}
