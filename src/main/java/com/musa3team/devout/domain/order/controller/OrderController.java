package com.musa3team.devout.domain.order.controller;

import com.musa3team.devout.common.config.OrderLogging;
import com.musa3team.devout.common.jwt.JwtUtil;
import com.musa3team.devout.domain.order.dto.request.ChangeStatusRequest;
import com.musa3team.devout.domain.order.dto.request.CreateOrderRequest;
import com.musa3team.devout.domain.order.dto.response.OrderResponse;
import com.musa3team.devout.domain.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Order 관리 API", description = "고객 - 메뉴를 주문 및 조회, -사장 주문 상태 변경, 조회 기능 API입니다.")
public class OrderController {

    private final OrderService orderService;
    private final JwtUtil jwtUtil;

    @OrderLogging
    @PostMapping("/orders")
    @Operation(summary = "주문 생성", description = "고객 유저가 메뉴를 주문할 수 있습니다.")
    public ResponseEntity<OrderResponse> createOrder(HttpServletRequest token, @Valid @RequestBody CreateOrderRequest request){
        System.out.println("auth : " + token.getHeader("Authorization"));
        System.out.println("substringToken : " + jwtUtil.substringToken(token.getHeader("Authorization")));
        Long tokenMemberId = jwtUtil.extractMemberId(jwtUtil.substringToken(token.getHeader("Authorization")));
        return ResponseEntity.ok(orderService.createOrder(tokenMemberId, request));
    }

    @OrderLogging
    @PatchMapping("/orders/{id}")
    @Operation(summary = "주문 상태 변경", description = "사장 유저가 주문 상태를 변경할 수 있습니다.")
    public void changeStatus(
            HttpServletRequest token,
            @PathVariable Long id,
            @Valid @RequestBody ChangeStatusRequest request){
        Long tokenMemberId = jwtUtil.extractMemberId(jwtUtil.substringToken(token.getHeader("Authorization")));
        orderService.changeOrderStatus(tokenMemberId, id, request.getOrderStatus());
    }

    // 고객
    @GetMapping("/orders/{id}")
    @Operation(summary = "고객-단건조회", description = "고객 유저가 주문한 정보를 확인할 수 있습니다.")
    public ResponseEntity<OrderResponse>customerFindById(
            HttpServletRequest token,
            @PathVariable Long id){
        Long tokenMemberId = jwtUtil.extractMemberId(jwtUtil.substringToken(token.getHeader("Authorization")));
        return ResponseEntity.ok(orderService.customerFindById(tokenMemberId, id));
    }

    @Operation(summary = "고객-다건조회", description = "고개 유저가 주문한 전체 정보를 확인할 수 있습니다.")
    @GetMapping("/orders")
    public ResponseEntity<Page<OrderResponse>>customerFindByAll(
            HttpServletRequest token,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size){
        Long tokenMemberId = jwtUtil.extractMemberId(jwtUtil.substringToken(token.getHeader("Authorization")));

        System.out.println("token " + token.getHeader("Authorization"));
        return ResponseEntity.ok(orderService.customerFindByAll(tokenMemberId, page, size));
    }

    @Operation(summary = "사장-단건조회", description = "사장 유저가 주문 정보를 확인할 수 있습니다.")
    @GetMapping("/{storeId}/orders/{id}")
    public ResponseEntity<OrderResponse>storeFindById(HttpServletRequest token,
                                                      @PathVariable Long id){
        Long tokenMemberId = jwtUtil.extractMemberId(jwtUtil.substringToken(token.getHeader("Authorization")));
        return ResponseEntity.ok(orderService.storeFindById(tokenMemberId, id));
    }

    @Operation(summary = "사장-다건조회", description = "사장 유저가 해당 가게로 주문받은 전체 정보를 확인할 수 있습니다.")
    @GetMapping("/{storeId}/orders")
    public ResponseEntity<Page<OrderResponse>>storeFindByAll(
            HttpServletRequest token,
            @PathVariable Long storeId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size){
        Long tokenMemberId = jwtUtil.extractMemberId(jwtUtil.substringToken(token.getHeader("Authorization")));
        return ResponseEntity.ok(orderService.storeFindByAll(tokenMemberId, storeId, page, size));
    }



}
