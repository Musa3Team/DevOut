package com.musa3team.devout.domain.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderItemRequest {
    @Schema(description = "메뉴ID", example = "1")
    private Long menuId;
    @Schema(description = "개수", example = "3")
    private int count;
    @Schema(description = "금액", example = "10000")
    private int price;
}