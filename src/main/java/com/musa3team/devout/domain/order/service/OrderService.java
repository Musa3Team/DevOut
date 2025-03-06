package com.musa3team.devout.domain.order.service;

import com.musa3team.devout.common.constants.StoreStatus;
import com.musa3team.devout.domain.member.entity.Member;
import com.musa3team.devout.domain.member.entity.MemberRole;
import com.musa3team.devout.domain.member.repository.MemberRepository;
import com.musa3team.devout.domain.menu.entity.Menu;
import com.musa3team.devout.domain.menu.repository.MenuRepository;
import com.musa3team.devout.domain.order.dto.request.CreateOrderRequest;
import com.musa3team.devout.domain.order.dto.response.OrderResponse;
import com.musa3team.devout.domain.order.entity.OrderItem;
import com.musa3team.devout.domain.order.entity.Orders;
import com.musa3team.devout.domain.order.enums.OrderStatus;
import com.musa3team.devout.domain.order.repository.OrderItemRepository;
import com.musa3team.devout.domain.order.repository.OrderRepository;
import com.musa3team.devout.domain.store.entity.Store;
import com.musa3team.devout.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.jboss.logging.MDC;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public OrderResponse createOrder(Long memberId, CreateOrderRequest request){

        Member member = memberRepository.findByIdAndMemberRole(memberId, MemberRole.CUSTOMER)
                .orElseThrow(() -> new IllegalArgumentException("고객인 유저만 주문이 가능합니다"));

        Store store = storeRepository.findByIdAndOpenTimeBeforeAndCloseTimeAfterAndStatus(request.getStoreId(), LocalTime.now(), LocalTime.now(), StoreStatus.OPEN)
                .orElseThrow(() -> new IllegalArgumentException("현재 영업중인 가게가 아닙니다."));

        Menu menu = menuRepository.findByIdAndStoreId(request.getOrderItem().getMenuId(), request.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("해당 가게에는 존재하지 않는 메뉴입니다."));

        if(menu.getPrice() != request.getOrderItem().getPrice()){
            throw new IllegalArgumentException("요청한 금액과 실제 금액이 맞지 않습니다. 다시 주문 부탁드립니다.");
        }

        int totalPrice = getTotalPrice(request.getOrderItem().getCount(), request.getOrderItem().getPrice());

        if(store.getMinimumPrice() > totalPrice){
            throw new IllegalArgumentException("최소 주문 금액을 충족하지 못했습니다. 다시 주문 부탁드립니다.");
        }

        Orders order =orderRepository.save(new Orders(request.getStoreId(), member));
        OrderItem orderItem = orderItemRepository.save(new OrderItem(menu.getName(),
                request.getOrderItem().getCount(),
                request.getOrderItem().getPrice(),
                order));

        setMDC(order);
        return OrderResponse.toDto(order, orderItem, totalPrice);
    }

    @Transactional
    public void changeOrderStatus(Long memberId, Long id, OrderStatus status){
        Orders order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 주문 정보입니다."));

        memberRepository.findByIdAndMemberRole(memberId, MemberRole.OWNER)
                .orElseThrow(() -> new IllegalArgumentException("사장인 유저만 주문 상태변경이 가능합니다"));

        if(!order.changeStatus(status)){
            throw new IllegalArgumentException(String.format("'%s' 상태에서 '%s' 상태로 변경할 수 없습니다.",
                    order.getStatus().getMessage(), status.getMessage()));
        }

        setMDC(order);
        order.updateStatus(status);
    }

    // 고객 단건 주문
    @Transactional(readOnly = true)
    public OrderResponse customerFindById(Long customerId, Long id){
        Orders order = orderRepository.findByIdAndMemberId(id, customerId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 주문 정보입니다."));
        OrderItem orderItem = orderItemRepository.findByOrderId(id)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 주문 정보입니다."));
        int totalPrice = getTotalPrice(orderItem.getCount(), orderItem.getPrice());

        return OrderResponse.toDto(order, orderItem, totalPrice);
    }

    // 사장 단건 주문
    @Transactional(readOnly = true)
    public OrderResponse storeFindById(Long ownerId, Long id){
        Store store = storeRepository.findByIdAndMemberId(id, ownerId)
                .orElseThrow(() -> new IllegalArgumentException("가게 정보가 존재하지 않습니다."));

        Orders order = orderRepository.findByIdAndStoreId(id, store.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 가게의 주문 정보가 아닙니다."));

        OrderItem orderItem = orderItemRepository.findByOrderId(id)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 주문 정보입니다."));

        int totalPrice = getTotalPrice(orderItem.getCount(), orderItem.getPrice());

        return OrderResponse.toDto(order, orderItem, totalPrice);
    }

    //    // 고객
    @Transactional(readOnly = true)
    public Page<OrderResponse> customerFindByAll(Long customerId, int page, int size){
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Orders> orderPage = orderRepository.findByMemberId(customerId, pageable);

        List<Orders> ordersList = orderPage.getContent();
        List<Long> orderIds = ordersList.stream().map(Orders::getId).collect(Collectors.toList());
        List<OrderItem> orderItems = orderItemRepository.findByOrderIdIn(orderIds);

        Map<Long, OrderItem> orderItemMap = orderItems.stream()
                .collect(Collectors.toMap(orderItem -> orderItem.getOrder().getId(), item -> item));


        List<OrderResponse> orderResponses = ordersList.stream().map(order -> {
            OrderItem orderItem = orderItemMap.get(order.getId());
            int totalPrice = getTotalPrice(orderItem.getCount(), orderItem.getPrice());
            return OrderResponse.toDto(order, orderItem, totalPrice);
        }).collect(Collectors.toList());

        return new PageImpl<>(orderResponses, pageable, orderPage.getTotalElements());
    }

    // 사장
    @Transactional(readOnly = true)
    public Page<OrderResponse> storeFindByAll(Long ownerId, Long storeId, int page, int size){
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Orders> orderPage = orderRepository.findByStoreIdAndMemberIdOrderByCreatedAtDesc(storeId, ownerId, pageable);

        List<Orders> ordersList = orderPage.getContent();
        List<Long> orderIds = ordersList.stream().map(Orders::getId).collect(Collectors.toList());
        List<OrderItem> orderItems = orderItemRepository.findByOrderIdIn(orderIds);

        // 주문 ID 기준으로 OrderItem을 매핑
        Map<Long, OrderItem> orderItemMap = orderItems.stream()
                .collect(Collectors.toMap(orderItem -> orderItem.getOrder().getId(), item -> item));

        List<OrderResponse> orderResponses = ordersList.stream().map(order -> {
            OrderItem orderItem = orderItemMap.get(order.getId());
            int totalPrice = getTotalPrice(orderItem.getCount(), orderItem.getPrice());
            return OrderResponse.toDto(order, orderItem, totalPrice);
        }).collect(Collectors.toList());

        return new PageImpl<>(orderResponses, pageable, orderPage.getTotalElements());
    }

    private int getTotalPrice(int count, int price){
        return count * price;
    }

    private void setMDC(Orders order){
        // aop값
        MDC.put("storeId", order.getStoreId());
        MDC.put("orderId", order.getId());
        MDC.put("status" , order.getStatus());
    }

}
