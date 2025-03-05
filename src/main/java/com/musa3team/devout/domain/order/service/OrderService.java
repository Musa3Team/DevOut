package com.musa3team.devout.domain.order.service;

import com.musa3team.devout.domain.member.entity.Member;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request){

        // todo : 테스트값 추후에 변경 필요
        Member member = memberRepository.findByIdAndMemberRole(2L, "CUSTOMER")
                .orElseThrow(() -> new IllegalArgumentException("고객인 유저만 주문이 가능합니다"));

        Store store = storeRepository.findByIdAndOpenTimeBeforeAndCloseTimeAfterAndStatus(request.getStoreId(), LocalTime.now(), LocalTime.now(), "OPEN")
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

        MDC.put("storeId", order.getStoreId());
        MDC.put("orderId", order.getId());
        MDC.put("status" , order.getStatus());

        return OrderResponse.toDto(order, orderItem, totalPrice);
    }

    @Transactional
    public void changeOrderStatus(Long id, OrderStatus status){
        Orders order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 주문 정보입니다."));

        // todo : 테스트값 추후에 변경 필요
        memberRepository.findByIdAndMemberRole(1L, "OWNER")
                .orElseThrow(() -> new IllegalArgumentException("사장인 유저만 주문 상태변경이 가능합니다"));

        if(order.getStatus().getSequence()+1 != status.getSequence() || (order.getStatus().getSequence() == 1 && status != OrderStatus.CANCELED)){
            throw new IllegalArgumentException(String.format("'%s' 상태에서 '%s' 상태로 변경할 수 없습니다.",
                    order.getStatus().getMessage(), status.getMessage()));
        }

        order.updateStatus(status);

        // aop값
        MDC.put("storeId", order.getStoreId());
        MDC.put("orderId", order.getId());
        MDC.put("status" , order.getStatus());
    }

    public Optional<Orders> findByIdAndStatus(Long orderId, OrderStatus status) {
        return orderRepository.findByIdAndStatus(orderId, status);
    }

    private int getTotalCount(int count){
        return count;
    }

    private int getTotalPrice(int count, int price){
        return count * price;
    }

}
