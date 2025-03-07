package com.musa3team.devout.domain.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    @Schema(description = "가게정보", example = "1")
    private Long storeId;
    private CreateOrderItemRequest orderItem;

}