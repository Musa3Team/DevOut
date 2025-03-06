package com.musa3team.devout.domain.review.service;

import com.musa3team.devout.common.constants.StoreCategory;
import com.musa3team.devout.common.constants.StoreStatus;
import com.musa3team.devout.common.entity.BaseEntity;
import com.musa3team.devout.domain.member.entity.Member;
import com.musa3team.devout.domain.member.entity.MemberRole;
import com.musa3team.devout.domain.member.repository.MemberRepository;
import com.musa3team.devout.domain.order.entity.Orders;
import com.musa3team.devout.domain.order.enums.OrderStatus;
import com.musa3team.devout.domain.order.repository.OrderRepository;
import com.musa3team.devout.domain.review.dto.request.CreateReviewRequestDto;
import com.musa3team.devout.domain.review.dto.request.UpdateReviewRequestDto;
import com.musa3team.devout.domain.review.dto.response.ReviewResponseDto;
import com.musa3team.devout.domain.review.entity.Review;
import com.musa3team.devout.domain.review.repository.ReviewRepository;
import com.musa3team.devout.domain.store.entity.Store;
import com.musa3team.devout.domain.store.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private StoreRepository storeRepository;

    private Orders order;
    private Member member;
    private Store store;
    private CreateReviewRequestDto createDto;
    private UpdateReviewRequestDto updateDto;

    @BeforeEach
    public void setUp() {
        member = new Member("customer", "customer@example.com", "customer", "customerAddress", "010-1111-5678", "CUSTOMER");
        setId(member, 99L);

        member = new Member("owner", "owner@example.com", "owner", "ownerAddress", "010-1234-5678", "OWNER");
        setId(member, 999L);

        store = new Store();
        store.setId(8L);

        order = new Orders(8L, member);
        setId(order, 10L);
        order.updateStatus(OrderStatus.DELIVERED);

        createDto = new CreateReviewRequestDto("contents", 1);
        updateDto = new UpdateReviewRequestDto("Updated contents", 4);
    }

    private void setId(Object entity, Long id) {
        try {
            Field field = entity.getClass().getDeclaredField("id");
            field.setAccessible(true);
            field.set(entity, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testSaveReview_success() {

        // given
        Long memberId = 1L;
        Member member = new Member("테스트명", "email@email.com", "passwrod",
                "010-1234-1234", "주소", MemberRole.CUSTOMER.name());
        ReflectionTestUtils.setField(member, "id", 1L);
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        long storeId = 1L;
        Store store = new Store("321-1234-1234", "주소", "가게내용", "가게명",
                LocalTime.of(8, 0, 0), LocalTime.of(20, 0, 0)
                , 20000, StoreCategory.CHICKEN, member);
        ReflectionTestUtils.setField(store, "status", StoreStatus.OPEN);
        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));

        Long orderId = 1L;
        Orders order = new Orders(1L, member);
        ReflectionTestUtils.setField(order, "id", orderId);
        ReflectionTestUtils.setField(order, "status", OrderStatus.DELIVERED);
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

        // when
        CreateReviewRequestDto createReviewRequestDto = new CreateReviewRequestDto("contents", 3);
        ReviewResponseDto responseDto = reviewService.save(memberId, orderId, createReviewRequestDto);

        // then
        assertNotNull(responseDto);

    }

    @Test
    void testGetReview_success() {
        // given
        Review review1 = new Review();
        setId(review1, 1L);
        review1.setOrder(order);
        review1.setMember(member);
        review1.setStore(store);
        review1.setContents("Review One");
        review1.setRating(4);
        ReflectionTestUtils.setField(review1, "createdAt", LocalDateTime.now().minusDays(1));

        Review review2 = new Review();
        setId(review2, 2L);
        review2.setOrder(order);
        review2.setMember(member);
        review2.setStore(store);
        review2.setContents("Review Two");
        review2.setRating(5);
        ReflectionTestUtils.setField(review2, "createdAt", LocalDateTime.now());

        given(reviewRepository.findByStoreIdAndRatingBetweenOrderByCreatedAtDesc(8L, 3, 5))
                .willReturn(Arrays.asList(review2, review1));

        // when
        List<ReviewResponseDto> result = reviewService.getReviewsByStore(8L, 3, 5);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Review Two", result.get(0).getContents());
        assertEquals("Review One", result.get(1).getContents());
    }

    @Test
    void testUpdateReview_success() {
        // given
        Review review = new Review();
        ReflectionTestUtils.setField(review, "id", 10L);
        review.setOrder(order);
        review.setMember(member);
        review.setStore(store);
        review.setContents("Old Content");
        review.setRating(3);

        given(reviewRepository.findById(10L)).willReturn(Optional.of(review));

        // when
        ReviewResponseDto responseDto = reviewService.updateReview(999L, 10L, updateDto);

        // then
        assertNotNull(responseDto);
        assertEquals("Updated contents", responseDto.getContents());
        assertEquals(4, responseDto.getRating());
    }

    @Test
    void testDeleteReview_noPermission() {
        // given
        Review review = new Review();
        ReflectionTestUtils.setField(review, "id", 20L);
        review.setOrder(order);
        review.setMember(member);
        review.setStore(store);
        review.setContents("Delete this review");
        review.setRating(4);

        given(reviewRepository.findById(20L)).willReturn(Optional.of(review));

        // when
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                reviewService.deleteReview(2L, 20L)
        );

        // then
        assertEquals("리뷰 삭제 권한이 없습니다.", exception.getMessage());
    }
}