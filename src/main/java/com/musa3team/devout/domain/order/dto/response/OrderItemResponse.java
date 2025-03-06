package com.musa3team.devout.domain.order.dto.response;
import com.musa3team.devout.domain.order.entity.OrderItem;
import lombok.Getter;

@Getter
public class OrderItemResponse {
    private String menuName;
    private int count;
    private int price;


    public OrderItemResponse(String menuName, int count, int price) {
        this.menuName = menuName;
        this.count = count;
        this.price = price;
    }

    public static OrderItemResponse toDto(OrderItem orderItem){
        return new OrderItemResponse(
                orderItem.getMenuName(),
                orderItem.getCount(),
                orderItem.getPrice()
        );
    }
}
