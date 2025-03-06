package com.musa3team.devout.domain.order.dto.request;

import com.musa3team.devout.domain.order.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ChangeStatusRequest {
    @Schema(description = "주문상태변경", example = "PREPARING")
    private OrderStatus orderStatus;

}
