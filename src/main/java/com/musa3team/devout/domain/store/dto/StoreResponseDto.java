package com.musa3team.devout.domain.store.dto;

import com.musa3team.devout.common.constants.StoreCategory;
import com.musa3team.devout.common.constants.StoreStatus;
import com.musa3team.devout.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class StoreResponseDto {
    private Long id;
    private String telephone_number;
    private String address;
    private String contents;
    private String name;
    private LocalTime open_time;
    private LocalTime close_time;
    private Long minimum_price;
    private StoreCategory category;
    private StoreStatus status;
}
