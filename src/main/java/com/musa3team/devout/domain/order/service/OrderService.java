package com.musa3team.devout.domain.order.service;

import com.musa3team.devout.domain.member.entity.Member;
import com.musa3team.devout.domain.member.repository.MemberRepository;
import com.musa3team.devout.domain.menu.entity.Menu;
import com.musa3team.devout.domain.menu.repository.MenuRepository;
import com.musa3team.devout.domain.order.dto.request.CreateOrderRequest;
import com.musa3team.devout.domain.order.dto.response.OrderItemResponse;
import com.musa3team.devout.domain.order.dto.response.OrderResponse;
import com.musa3team.devout.domain.order.entity.OrderItem;
import com.musa3team.devout.domain.order.entity.Orders;
import com.musa3team.devout.domain.order.enums.OrderStatus;
import com.musa3team.devout.domain.order.repository.OrderItemRepository;
import com.musa3team.devout.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final MemberRepository memberRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request){

        Member member = memberRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 정보입니다."));

        Menu menu = menuRepository.findById(request.getOrderItem().getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));

        Orders order =orderRepository.save( new Orders(request.getStoreId(), OrderStatus.PENDING, member));

        OrderItem orderItem = orderItemRepository.save(new OrderItem(menu.getName(),
                request.getOrderItem().getCount(),
                request.getOrderItem().getPrice(),
                order));

        return new OrderResponse(order.getId(),
                order.getStoreId(),
                2,
                10000,
                order.getStatus(),
                new OrderItemResponse(orderItem.getMenuName(), orderItem.getCount(), orderItem.getPrice()));
    }

}
