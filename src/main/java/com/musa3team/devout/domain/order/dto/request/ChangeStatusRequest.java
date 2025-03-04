package com.musa3team.devout.domain.order.dto.request;

import com.musa3team.devout.domain.order.enums.OrderStatus;
import lombok.Getter;

@Getter
public class ChangeStatusRequest {
    private OrderStatus orderStatus;

}
