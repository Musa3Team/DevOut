package com.musa3team.devout.store;

import com.musa3team.devout.common.config.PasswordEncoder;
import com.musa3team.devout.common.constants.StoreCategory;
import com.musa3team.devout.common.constants.StoreStatus;
import com.musa3team.devout.domain.member.entity.Member;
import com.musa3team.devout.domain.member.repository.MemberRepository;
import com.musa3team.devout.domain.store.dto.StoreResponseDto;
import com.musa3team.devout.domain.store.entity.Store;
import com.musa3team.devout.domain.store.repository.StoreRepository;
import com.musa3team.devout.domain.store.service.StoreService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private StoreService storeService;

    @Test
    void Store를_영업준비한다() {
        //given
        Long id = 1L;
        StoreStatus status = StoreStatus.UNPREPARED;
        LocalTime openTime = LocalTime.now();
        LocalTime closeTime = LocalTime.now();

        Store store = new Store();
        ReflectionTestUtils.setField(store, "id", id);
        ReflectionTestUtils.setField(store, "openTime", openTime);
        ReflectionTestUtils.setField(store, "closeTime", closeTime);
        ReflectionTestUtils.setField(store, "status", status);

        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
        //when
        StoreResponseDto prepare = storeService.prepare(id);

        //then
        assertThat(prepare).isNotNull();
        assertThat(prepare.getId()).isEqualTo(id);
        assertThat(prepare.getStatus()).isEqualTo(StoreStatus.OPEN);
    }

    @Test
    void Store를_수정한다() {
        //given
        Long id = 1L;
        StoreStatus status = StoreStatus.CLOSE;
        LocalTime openTime = LocalTime.of(13,0);
        LocalTime closeTime = LocalTime.of(21,0);

        Store store = new Store();
        ReflectionTestUtils.setField(store, "id", id);
        ReflectionTestUtils.setField(store, "openTime", openTime);
        ReflectionTestUtils.setField(store, "closeTime", closeTime);
        ReflectionTestUtils.setField(store, "status", status);

        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));

        //when
        StoreResponseDto update = storeService.update(id, "주소", StoreCategory.CHICKEN, "치킨", "이쿠", 20000L, "032-000-0000", openTime, closeTime);

        //then
        assertThat(update.getId()).isEqualTo(id);
        assertThat(update.getAddress()).isEqualTo("주소");
        assertThat(update.getCategory()).isEqualTo(StoreCategory.CHICKEN);
        assertThat(update.getName()).isEqualTo("치킨");
        assertThat(update.getContents()).isEqualTo("이쿠");
        assertThat(update.getMinimum_price()).isEqualTo(20000L);
        assertThat(update.getTelephone_number()).isEqualTo("032-000-0000");
        assertThat(update.getOpen_time()).isEqualTo(openTime);
        assertThat(update.getClose_time()).isEqualTo(closeTime);
    }

    @Test
    void Store를_폐업한다() {
        //given
        Long storeId = 1L;
        Long memberId = 1L;
        String password = "1234!a5678";

        Store store = new Store();
        ReflectionTestUtils.setField(store, "id", storeId);
        ReflectionTestUtils.setField(store, "status", StoreStatus.CLOSE);

        Member member = new Member();
        ReflectionTestUtils.setField(member, "id", memberId);
        ReflectionTestUtils.setField(member, "password", passwordEncoder.encode(password));

        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
        given(memberRepository.findByIdOrElseThrow(anyLong())).willReturn(member);
        given(passwordEncoder.matches(password, member.getPassword())).willReturn(true);

        //when
        storeService.delete(storeId, memberId, password);

        //then
        assertThat(store.getStatus()).isEqualTo(StoreStatus.SHUTDOWN);
    }
}
