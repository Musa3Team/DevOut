package com.musa3team.devout.domain.order.controller;

import com.musa3team.devout.domain.order.dto.request.ChangeStatusRequest;
import com.musa3team.devout.domain.order.dto.request.CreateOrderRequest;
import com.musa3team.devout.domain.order.dto.response.OrderResponse;
import com.musa3team.devout.domain.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    //todo : 추후 jwt 토큰 추가
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request){
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @PatchMapping("/{id}")
    public void changeStatus(@PathVariable Long id, @Valid @RequestBody ChangeStatusRequest requset){
        orderService.changeOrderStatus(id, requset.getOrderStatus());
    }
}
