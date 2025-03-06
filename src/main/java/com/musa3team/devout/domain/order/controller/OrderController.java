package com.musa3team.devout.domain.order.controller;

import com.musa3team.devout.domain.order.dto.request.ChangeStatusRequest;
import com.musa3team.devout.common.config.OrderLogging;
import com.musa3team.devout.common.jwt.JwtUtil;
import com.musa3team.devout.domain.order.dto.request.CreateOrderRequest;
import com.musa3team.devout.domain.order.dto.response.OrderResponse;
import com.musa3team.devout.domain.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final JwtUtil jwtUtil;

    @OrderLogging
    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> createOrder(HttpServletRequest token, @Valid @RequestBody CreateOrderRequest request){
        System.out.println("auth : " + token.getHeader("Authorization"));
        System.out.println("substringToken : " + jwtUtil.substringToken(token.getHeader("Authorization")));
        Long tokenMemberId = jwtUtil.extractMemberId(jwtUtil.substringToken(token.getHeader("Authorization")));
        return ResponseEntity.ok(orderService.createOrder(tokenMemberId, request));
    }

    @OrderLogging
    @PatchMapping("/orders/{id}")
    public void changeStatus(
            HttpServletRequest token,
            @PathVariable Long id,
            @Valid @RequestBody ChangeStatusRequest request){
        Long tokenMemberId = jwtUtil.extractMemberId(jwtUtil.substringToken(token.getHeader("Authorization")));
        orderService.changeOrderStatus(tokenMemberId, id, request.getOrderStatus());
    }

    // 고객
    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderResponse>customerFindById(
            HttpServletRequest token,
            @PathVariable Long id){
        Long tokenMemberId = jwtUtil.extractMemberId(jwtUtil.substringToken(token.getHeader("Authorization")));
        return ResponseEntity.ok(orderService.customerFindById(tokenMemberId, id));
    }

    @GetMapping("/orders")
    public ResponseEntity<Page<OrderResponse>>customerFindByAll(
            HttpServletRequest token,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size){
        Long tokenMemberId = jwtUtil.extractMemberId(jwtUtil.substringToken(token.getHeader("Authorization")));

        System.out.println("token " + token.getHeader("Authorization"));
        return ResponseEntity.ok(orderService.customerFindByAll(tokenMemberId, page, size));
    }

    @GetMapping("/{storeId}/orders/{id}")
    public ResponseEntity<OrderResponse>storeFindById(HttpServletRequest token,
                                                      @PathVariable Long id){
        Long tokenMemberId = jwtUtil.extractMemberId(jwtUtil.substringToken(token.getHeader("Authorization")));
        return ResponseEntity.ok(orderService.storeFindById(tokenMemberId, id));
    }

    @GetMapping("/{storeId}/orders")
    public ResponseEntity<Page<OrderResponse>>storeFindByAll(
            HttpServletRequest token,
            @PathVariable Long storeId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size){
        Long tokenMemberId = jwtUtil.extractMemberId(jwtUtil.substringToken(token.getHeader("Authorization")));
        return ResponseEntity.ok(orderService.storeFindByAll(tokenMemberId, storeId, page, size));
    }

    @PatchMapping("/{id}")
    public void changeStatus(@PathVariable Long id, @Valid @RequestBody ChangeStatusRequest requset){
        orderService.changeOrderStatus(id, requset.getOrderStatus());
    }



}
