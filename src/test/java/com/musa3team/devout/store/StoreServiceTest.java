package com.musa3team.devout.store;

import com.musa3team.devout.common.constants.StoreStatus;
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

    @InjectMocks
    private StoreService storeService;

    @Test
    void Store를_영업준비한다() {
        //given
        Long id = 1L;
        StoreStatus status = StoreStatus.UNPREPARED;
        LocalTime openTime = LocalTime.of(13,0);
        LocalTime closeTime = LocalTime.of(21,0);
        Store store = new Store(status, openTime, closeTime);
        ReflectionTestUtils.setField(store, "id", id);

        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
        //when
        StoreResponseDto prepare = storeService.prepare(id);

        //then
        assertThat(prepare).isNotNull();
        assertThat(prepare.getId()).isEqualTo(id);
        assertThat(prepare.getStatus()).isEqualTo(StoreStatus.CLOSE);
    }
}
