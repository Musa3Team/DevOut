package com.musa3team.devout.domain.order.service;

import com.musa3team.devout.common.constants.StoreCategory;
import com.musa3team.devout.common.constants.StoreStatus;
import com.musa3team.devout.domain.member.entity.Member;
import com.musa3team.devout.domain.member.entity.MemberRole;
import com.musa3team.devout.domain.member.repository.MemberRepository;
import com.musa3team.devout.domain.menu.entity.Menu;
import com.musa3team.devout.domain.menu.entity.MenuCategory;
import com.musa3team.devout.domain.menu.entity.MenuStatus;
import com.musa3team.devout.domain.menu.repository.MenuRepository;
import com.musa3team.devout.domain.order.dto.request.CreateOrderItemRequest;
import com.musa3team.devout.domain.order.dto.request.CreateOrderRequest;
import com.musa3team.devout.domain.order.dto.response.OrderResponse;
import com.musa3team.devout.domain.order.entity.OrderItem;
import com.musa3team.devout.domain.order.entity.Orders;
import com.musa3team.devout.domain.order.enums.OrderStatus;
import com.musa3team.devout.domain.order.repository.OrderItemRepository;
import com.musa3team.devout.domain.order.repository.OrderRepository;
import com.musa3team.devout.domain.store.entity.Store;
import com.musa3team.devout.domain.store.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceTest.class);
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void 주문_성공(){
        //given
        Long memberId = 1L;
        Member member = new Member("테스트명", "email@email.com", "passwrod",
                "010-1234-1234", "주소", MemberRole.CUSTOMER.name());
        ReflectionTestUtils.setField(member, "id", 1L);

        long storeId = 1L;
        Store store = new Store("321-1234-1234", "주소", "가게내용", "가게명",
                LocalTime.of(8,0,0), LocalTime.of(20,0,0)
                , 20000, StoreCategory.CHICKEN, member);
        ReflectionTestUtils.setField(store, "status", StoreStatus.OPEN);

        long menuId = 1L;
        int count = 2;
        int price = 10000;
        Menu menu = new Menu(store, "메뉴명", 10000, "메뉴내용", MenuCategory.DRINKS);
        ReflectionTestUtils.setField(menu, "id", 1L);

        Orders order = new Orders(storeId, member);
        OrderItem orderItem = new OrderItem(menu.getName(), count, price, order);
        int totalPrice = count * price;

        CreateOrderItemRequest orderItemRequest = new CreateOrderItemRequest(menuId, count, price);
        CreateOrderRequest request = new CreateOrderRequest(storeId, orderItemRequest);

        given(memberRepository.findByIdAndMemberRole(anyLong(), eq(MemberRole.CUSTOMER))).willReturn(Optional.of(member));
        given(storeRepository.findByIdAndOpenTimeBeforeAndCloseTimeAfterAndStatus(
                anyLong(),any(LocalTime.class), any(LocalTime.class), eq(StoreStatus.OPEN))
              ).willReturn(Optional.of(store));
        given(menuRepository.findByIdAndStoreId(anyLong(), anyLong())).willReturn(Optional.of(menu));

        given(orderRepository.save(any(Orders.class))).willReturn(order);
        given(orderItemRepository.save(any(OrderItem.class))).willReturn(orderItem);

        // When
        OrderResponse response = orderService.createOrder(memberId, request);

        // Then
        assertNotNull(response);
        assertEquals(storeId, response.getStoreId());
        assertEquals(menu.getName(), response.getOrderItem().getMenuName());
        assertEquals(count, response.getOrderItem().getCount());
        assertEquals(price, response.getOrderItem().getPrice());
        assertEquals(totalPrice, response.getTotalPrice());

        // Verify 검증
        verify(memberRepository, times(1)).findByIdAndMemberRole(eq(1L), eq(MemberRole.CUSTOMER));
        verify(storeRepository, times(1))
                .findByIdAndOpenTimeBeforeAndCloseTimeAfterAndStatus(eq(storeId), any(LocalTime.class), any(LocalTime.class), eq(StoreStatus.OPEN));
        verify(menuRepository, times(1)).findByIdAndStoreId(eq(menuId), eq(storeId));
        verify(orderRepository, times(1)).save(any(Orders.class));
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));

    }

    @Test
    void 최소금액을_충족하지_못할_경우_예외처리(){
        //given
        long memberId = 1L;
        Member member = new Member("테스트명", "email@email.com", "passwrod", "010-1234-1234", "주소", "OWNER");
        ReflectionTestUtils.setField(member, "id", memberId);

        long storeId = 1L;
        Store store = new Store("321-1234-1234", "주소", "가게내용", "가게명",
                LocalTime.of(8,0,0), LocalTime.of(20,0,0)
                , 20000, StoreCategory.CHICKEN, member);
        ReflectionTestUtils.setField(store, "status", StoreStatus.OPEN);

        long menuId = 1L;
        Menu menu = new Menu(store, "메뉴명", 10000, "메뉴내용", MenuCategory.DRINKS);
        ReflectionTestUtils.setField(menu, "id", menuId);

        int count = 1;
        int price = 10000;
        CreateOrderItemRequest orderItemRequest = new CreateOrderItemRequest(menuId, count, price);
        CreateOrderRequest request = new CreateOrderRequest(storeId, orderItemRequest);

        given(memberRepository.findByIdAndMemberRole(anyLong(), eq(MemberRole.CUSTOMER))).willReturn(Optional.of(member));
        given(storeRepository.findByIdAndOpenTimeBeforeAndCloseTimeAfterAndStatus(anyLong(),any(LocalTime.class), any(LocalTime.class), eq(StoreStatus.OPEN))).willReturn(Optional.of(store));
        given(menuRepository.findByIdAndStoreId(anyLong(), anyLong())).willReturn(Optional.of(menu));



        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.createOrder(memberId, request));

        //then
        assertEquals("최소 주문 금액을 충족하지 못했습니다. 다시 주문 부탁드립니다.", exception.getMessage());
    }

    @Test
    void 요청한_금액과_실제금액이_다른_경우_예외처리(){
        //given
        long memberId = 1L;
        Member member = new Member("테스트명", "email@email.com", "passwrod", "010-1234-1234", "주소", "OWNER");
        ReflectionTestUtils.setField(member, "id", memberId);

        long storeId = 1L;
        Store store = new Store("321-1234-1234", "주소", "가게내용", "가게명",
                LocalTime.of(8,0,0), LocalTime.of(20,0,0)
                , 20000, StoreCategory.CHICKEN, member);
        ReflectionTestUtils.setField(store, "status", StoreStatus.OPEN);

        long menuId = 1L;
        Menu menu = new Menu(store, "메뉴명", 10000, "메뉴내용", MenuCategory.DRINKS);
        ReflectionTestUtils.setField(menu, "id", 1L);

        int count = 2;
        int price = 2000;
        CreateOrderItemRequest orderItemRequest = new CreateOrderItemRequest(menuId, count, price);

        CreateOrderRequest request = new CreateOrderRequest(storeId, orderItemRequest);
        given(memberRepository.findByIdAndMemberRole(anyLong(), eq(MemberRole.CUSTOMER))).willReturn(Optional.of(member));
        given(storeRepository.findByIdAndOpenTimeBeforeAndCloseTimeAfterAndStatus(anyLong(),any(LocalTime.class), any(LocalTime.class), eq(StoreStatus.OPEN))).willReturn(Optional.of(store));
        given(menuRepository.findByIdAndStoreId(anyLong(), anyLong())).willReturn(Optional.of(menu));

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.createOrder(memberId, request));

        //then
        assertEquals("요청한 금액과 실제 금액이 맞지 않습니다. 다시 주문 부탁드립니다.", exception.getMessage());
    }

    @Test
    void 가게메뉴가_아닌_경우_예외처리(){
        //given
        long memberId = 1L;
        Member member = new Member("테스트명", "email@email.com", "passwrod", "010-1234-1234", "주소", "OWNER");
        ReflectionTestUtils.setField(member, "id", memberId);

        long storeId = 1L;
        Store store = new Store("321-1234-1234", "주소", "가게내용", "가게명",
                LocalTime.of(8,0,0), LocalTime.of(20,0,0)
                , 20000, StoreCategory.CHICKEN, member);
        ReflectionTestUtils.setField(store, "status", StoreStatus.OPEN);

        int count = 2;
        int price = 2000;
        Long menuId = 1L;
        CreateOrderItemRequest orderItemRequest = new CreateOrderItemRequest(menuId, count, price);
        CreateOrderRequest request = new CreateOrderRequest(storeId, orderItemRequest);

        given(memberRepository.findByIdAndMemberRole(anyLong(), eq(MemberRole.CUSTOMER))).willReturn(Optional.of(member));
        given(storeRepository.findByIdAndOpenTimeBeforeAndCloseTimeAfterAndStatus(anyLong(), any(LocalTime.class), any(LocalTime.class), eq(StoreStatus.OPEN))).willReturn(Optional.of(store));
        given(menuRepository.findByIdAndStoreId(anyLong(), anyLong())).willReturn(Optional.empty());

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.createOrder(memberId, request));

        //then
        assertEquals("해당 가게에는 존재하지 않는 메뉴입니다.", exception.getMessage());
    }

    @Test
    void 영업중인_가게가_아닌_경우_예외처리(){
        //given
        long memberId = 1L;
        Member member = new Member("테스트명", "email@email.com", "passwrod", "010-1234-1234", "주소", "OWNER");
        ReflectionTestUtils.setField(member, "id", memberId);

        long storeId = 1L;
        long menuId = 1L;
        int count = 2;
        int price = 2000;
        CreateOrderItemRequest orderItemRequest = new CreateOrderItemRequest(menuId, count, price);
        CreateOrderRequest request = new CreateOrderRequest(storeId, orderItemRequest);

        given(memberRepository.findByIdAndMemberRole(anyLong(), eq(MemberRole.CUSTOMER))).willReturn(Optional.of(member));
        given(storeRepository.findByIdAndOpenTimeBeforeAndCloseTimeAfterAndStatus(anyLong(), any(LocalTime.class), any(LocalTime.class), eq(StoreStatus.OPEN))).willReturn(Optional.empty());

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.createOrder(memberId, request));

        //then
        assertEquals("현재 영업중인 가게가 아닙니다.", exception.getMessage());
    }

    @Test
    void 주문_고객_유저가_아닌_경우_예외처리(){
        //given
        long storeId = 1L;
        long menuId = 1L;
        int count = 2;
        int price = 2000;
        long memberId = 1L;
        Member member = new Member("테스트명", "email@email.com", "passwrod", "010-1234-1234", "주소", "OWNER");
        ReflectionTestUtils.setField(member, "id", memberId);

        CreateOrderItemRequest orderItemRequest = new CreateOrderItemRequest(menuId, count, price);
        CreateOrderRequest request = new CreateOrderRequest(storeId, orderItemRequest);

        given(memberRepository.findByIdAndMemberRole(anyLong(), eq(MemberRole.CUSTOMER))).willReturn(Optional.empty());

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.createOrder(memberId, request));

        //then
        assertEquals("고객인 유저만 주문이 가능합니다", exception.getMessage());
    }


    @Test
    void 주문_정보가_없을경우_예외처리(){

        long id = 1L;
        OrderStatus orderStatus = OrderStatus.PENDING;
        long memberId = 1L;
        Member member = new Member("테스트명", "email@email.com", "passwrod", "010-1234-1234", "주소", "OWNER");
        ReflectionTestUtils.setField(member, "id", memberId);

        //given
        given(orderRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.changeOrderStatus(memberId, id, orderStatus));

        //then
        assertEquals("잘못된 주문 정보입니다.", exception.getMessage());
    }

    @Test
    void 상태_변경시_유저가_사장이_아닌_경우_예외처리(){
        long id = 1L;
        long memberId = 1L;
        Member member = new Member("테스트명", "email@email.com", "passwrod", "010-1234-1234", "주소", "OWNER");
        ReflectionTestUtils.setField(member, "id", memberId);

        OrderStatus orderStatus = OrderStatus.PENDING;
        Orders order = new Orders(id, 1L, orderStatus, member);
        //given
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));
        given(memberRepository.findByIdAndMemberRole(anyLong(), eq(MemberRole.OWNER)))
                .willReturn(Optional.empty());

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.changeOrderStatus(memberId, id, orderStatus));

        //then
        assertEquals("사장인 유저만 주문 상태변경이 가능합니다", exception.getMessage());
    }

    @Test
    void 상태_변경이_순서대로_이루어지지_않은_경우_예외처리(){
        //given
        long ownerId = 1L;
        Member owner = new Member("테스트명", "email@email.com", "passwrod", "010-1234-1234", "주소", MemberRole.OWNER.name());
        ReflectionTestUtils.setField(owner, "id", ownerId);

        long id = 1L;
        OrderStatus orderStatus = OrderStatus.DELIVERED;
        Orders order = new Orders(1L, owner);
        ReflectionTestUtils.setField(order, "id", id);
        ReflectionTestUtils.setField(order, "status", OrderStatus.PENDING);

        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));
        given(memberRepository.findByIdAndMemberRole(anyLong(), eq(MemberRole.OWNER)))
                .willReturn(Optional.of(owner));

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.changeOrderStatus(ownerId, id, orderStatus));

        //then
        assertEquals(String.format("'%s' 상태에서 '%s' 상태로 변경할 수 없습니다.",
                order.getStatus().getMessage(), orderStatus.getMessage()), exception.getMessage());
    }

    @Test
    void 상태_변경이_성공(){

        long memberId = 1L;
        Member member = new Member("테스트명", "email@email.com", "passwrod", "010-1234-1234", "주소", "OWNER");
        ReflectionTestUtils.setField(member, "id", memberId);

        long id = 1L;
        OrderStatus orderStatus = OrderStatus.PREPARING;
        Orders order = new Orders(1L, member);
        ReflectionTestUtils.setField(order, "id", id);
        ReflectionTestUtils.setField(order, "status", OrderStatus.PENDING);
        //given
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));
        given(memberRepository.findByIdAndMemberRole(anyLong(), eq(MemberRole.OWNER)))
                .willReturn(Optional.of(member));

        //when
        orderService.changeOrderStatus(memberId, id, orderStatus);

        //then
        assertEquals(orderStatus, order.getStatus()); // 상태가 정상적으로 변경되었는지 확인

        // Repository 호출 검증
        verify(orderRepository, times(1)).findById(order.getId());
        verify(memberRepository, times(1)).findByIdAndMemberRole(1L, MemberRole.OWNER);
    }

    @Test
    void 고객_단건_주문_조회_실패_존재하지_않는_주문() {

        // given
        long memberId = 1L;
        Member member = new Member("테스트명", "email@email.com", "passwrod", "010-1234-1234", "주소", "OWNER");
        ReflectionTestUtils.setField(member, "id", memberId);

        long orderId = 1L;
        given(orderRepository.findByIdAndMemberId(anyLong(), anyLong())).willReturn(Optional.empty());

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.customerFindById(memberId, orderId));

        //then
        assertEquals("잘못된 주문 정보입니다.", exception.getMessage());
    }

    @Test
    void 고객_단건_주문_조회_성공() {
        // given
        long orderId = 1L;

        long customerId = 2L;
        Member member = new Member("테스트명", "email@email.com", "passwrod", "010-1234-1234", "주소", "OWNER");
        ReflectionTestUtils.setField(member, "id", customerId);

        Orders order = new Orders(1L, member);
        ReflectionTestUtils.setField(order, "id", orderId);

        OrderItem orderItem = new OrderItem("메뉴명", 2, 10000, order); // 2개 주문, 개당 10,000원

        given(orderRepository.findByIdAndMemberId(anyLong(), anyLong())).willReturn(Optional.of(order));
        given(orderItemRepository.findByOrderId(anyLong())).willReturn(Optional.of(orderItem));

        // when
        OrderResponse response = orderService.customerFindById(customerId, orderId);

        // then
        assertNotNull(response);
        assertEquals(orderId, response.getId());
        assertEquals(20000, response.getTotalPrice());
    }

    @Test
    void 사장_단건_주문_조회_실패_가게_존재하지_않음() {
        // given
        Long memberId = 1L;
        Member member = new Member("테스트명", "email@email.com", "passwrod", "010-1234-1234", "주소", "OWNER");
        ReflectionTestUtils.setField(member, "id", memberId);

        long orderId = 1L;
        given(storeRepository.findByIdAndMemberId(anyLong(), anyLong())).willReturn(Optional.empty());

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.storeFindById(memberId, orderId));

        // then
        assertEquals("가게 정보가 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    void 사장_단건_주문_조회_실패_해당_가게의_주문이_아님() {
        // given
        long orderId = 99L;

        long memberId = 1L;
        Member owner = new Member("사장", "owner@email.com", "password", "010-9876-5432", "주소", "OWNER");
        ReflectionTestUtils.setField(owner, "id", memberId);

        long storeId = 1L;
        Store store = new Store("321-1234-1234", "주소", "가게내용", "가게명",
                LocalTime.of(8,0,0), LocalTime.of(20,0,0)
                , 20000, StoreCategory.CHICKEN, owner);

        ReflectionTestUtils.setField(store, "id", storeId);
        ReflectionTestUtils.setField(store, "status", StoreStatus.OPEN);

        given(storeRepository.findByIdAndMemberId(anyLong(), anyLong())).willReturn(Optional.of(store));
        given(orderRepository.findByIdAndStoreId(anyLong(), anyLong())).willReturn(Optional.empty());

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.storeFindById(memberId, orderId));

        // then
        assertEquals("해당 가게의 주문 정보가 아닙니다.", exception.getMessage());
    }

    @Test
    void 사장_단건_주문_조회_성공() {
        // given
        long ownerId = 1L;
        Member owner = new Member("사장", "owner@email.com", "password", "010-9876-5432", "주소", "OWNER");
        ReflectionTestUtils.setField(owner, "id", ownerId);

        long storeId = 1L;
        Store store = new Store("321-1234-1234", "주소", "가게내용", "가게명",
                LocalTime.of(8,0,0), LocalTime.of(20,0,0)
                , 20000, StoreCategory.CHICKEN, owner);
        ReflectionTestUtils.setField(store, "id", storeId);
        ReflectionTestUtils.setField(store, "status", StoreStatus.OPEN);

        long orderId = 1L;
        Orders order = new Orders(storeId, owner);
        ReflectionTestUtils.setField(order, "id", orderId);

        OrderItem orderItem = new OrderItem("메뉴명", 3, 15000, order); // 3개 주문, 개당 15,000원

        given(storeRepository.findByIdAndMemberId(anyLong(), anyLong())).willReturn(Optional.of(store));
        given(orderRepository.findByIdAndStoreId(anyLong(), anyLong())).willReturn(Optional.of(order));
        given(orderItemRepository.findByOrderId(anyLong())).willReturn(Optional.of(orderItem));

        // when
        OrderResponse response = orderService.storeFindById(ownerId, orderId);

        // then
        assertNotNull(response);
        assertEquals(orderId, response.getId());
        assertEquals(45000, response.getTotalPrice());
    }

    @Test
    void 고객_전체_주문_조회_실패_주문_없음() {
        // given
        int page = 1;
        int size = 2;
        Pageable pageable = PageRequest.of(page - 1, size);

        long memberId = 1L;
        Member owner = new Member("사장", "owner@email.com", "password", "010-9876-5432", "주소", "OWNER");
        ReflectionTestUtils.setField(owner, "id", memberId);

        Page<Orders> emptyOrderPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        given(orderRepository.findByMemberId(anyLong(), eq(pageable))).willReturn(emptyOrderPage);
        given(orderItemRepository.findByOrderIdIn(Collections.emptyList())).willReturn(Collections.emptyList());

        // when
        Page<OrderResponse> responsePage = orderService.customerFindByAll(memberId, page, size);

        // then
        assertNotNull(responsePage);
        assertEquals(0, responsePage.getTotalElements());
    }

    @Test
    void 고객_전체_주문_조회_성공() {
        // given
        int page = 1;
        int size = 2;
        Pageable pageable = PageRequest.of(page - 1, size);
        long customerId = 2L;

        Orders order1 = new Orders(1L, new Member("고객", "customer@email.com", "password", "010-1234-1234", "주소", "CUSTOMER"));
        Orders order2 = new Orders(2L, new Member("고객", "customer@email.com", "password", "010-1234-1234", "주소", "CUSTOMER"));
        ReflectionTestUtils.setField(order1, "id", 1L);
        ReflectionTestUtils.setField(order2, "id", 2L);

        List<Orders> orderList = List.of(order1, order2);
        Page<Orders> orderPage = new PageImpl<>(orderList, pageable, orderList.size());

        OrderItem orderItem1 = new OrderItem("메뉴1", 1, 10000, order1);
        OrderItem orderItem2 = new OrderItem("메뉴2", 2, 15000, order2);

        given(orderRepository.findByMemberId(anyLong(), eq(pageable))).willReturn(orderPage);
        given(orderItemRepository.findByOrderIdIn(anyList())).willReturn(List.of(orderItem1, orderItem2));

        // when
        Page<OrderResponse> responsePage = orderService.customerFindByAll(customerId, page, size);

        // then
        assertNotNull(responsePage);
        assertEquals(2, responsePage.getTotalElements());
        assertEquals(10000, responsePage.getContent().get(0).getTotalPrice());
        assertEquals(30000, responsePage.getContent().get(1).getTotalPrice());
    }

    @Test
    void 사장_전체_주문_조회_실패_주문_없음() {
        // given
        int page = 1;
        int size = 2;
        long storeId = 1L;
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Orders> emptyOrderPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        long memberId = 1L;
        Member owner = new Member("사장", "owner@email.com", "password", "010-9876-5432", "주소", "OWNER");
        ReflectionTestUtils.setField(owner, "id", memberId);

        given(orderRepository.findByStoreIdAndMemberIdOrderByCreatedAtDesc(anyLong(), anyLong(), eq(pageable))).willReturn(emptyOrderPage);
        given(orderItemRepository.findByOrderIdIn(Collections.emptyList())).willReturn(Collections.emptyList());

        // when
        Page<OrderResponse> responsePage = orderService.storeFindByAll(memberId, storeId, page, size);

        // then
        assertNotNull(responsePage);
        assertEquals(0, responsePage.getTotalElements());
    }

    @Test
    void 사장_전체_주문_조회_성공() {
        // given
        int page = 1;
        int size = 2;
        long storeId = 1L;
        long ownerId = 2L;
        Pageable pageable = PageRequest.of(page - 1, size);

        Member owner = new Member("사장", "owner@email.com", "password", "010-9876-5432", "주소", "OWNER");
        ReflectionTestUtils.setField(owner, "id", ownerId);

        Orders order1 = new Orders(storeId, new Member("사장님", "owner@email.com", "password", "010-9876-5432", "주소", "OWNER"));
        Orders order2 = new Orders(storeId, new Member("사장님", "owner@email.com", "password", "010-9876-5432", "주소", "OWNER"));
        ReflectionTestUtils.setField(order1, "id", 1L);
        ReflectionTestUtils.setField(order2, "id", 2L);

        List<Orders> orderList = List.of(order1, order2);
        Page<Orders> orderPage = new PageImpl<>(orderList, pageable, orderList.size());

        OrderItem orderItem1 = new OrderItem("메뉴1", 1, 10000, order1);
        OrderItem orderItem2 = new OrderItem("메뉴2", 2, 20000, order2);

        given(orderRepository.findByStoreIdAndMemberIdOrderByCreatedAtDesc(anyLong(), anyLong(), eq(pageable))).willReturn(orderPage);
        given(orderItemRepository.findByOrderIdIn(anyList())).willReturn(List.of(orderItem1, orderItem2));

        // when
        Page<OrderResponse> responsePage = orderService.storeFindByAll(ownerId, storeId, page, size);

        // then
        assertNotNull(responsePage);
        assertEquals(2, responsePage.getTotalElements());
        assertEquals(10000, responsePage.getContent().get(0).getTotalPrice());
        assertEquals(40000, responsePage.getContent().get(1).getTotalPrice());
    }

}