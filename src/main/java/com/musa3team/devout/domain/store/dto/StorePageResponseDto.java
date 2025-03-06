package com.musa3team.devout.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class StorePageResponseDto {
    List<StoreResponseDto> stores;
    int currentPageNum;
    int totalPages;
    long totalElements;
}
