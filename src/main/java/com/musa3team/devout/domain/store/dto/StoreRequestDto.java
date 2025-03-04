package com.musa3team.devout.domain.store.dto;

import com.musa3team.devout.common.constants.StoreCategory;
import com.musa3team.devout.domain.store.valid.ValidTenMinuteInterval;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class StoreRequestDto {
    private String telephone_number;
    private String address;
    private String contents;
    private String name;
    @ValidTenMinuteInterval
    private LocalTime open_time;
    @ValidTenMinuteInterval
    private LocalTime close_time;
    private Long minimum_price;
    private StoreCategory category;
}
