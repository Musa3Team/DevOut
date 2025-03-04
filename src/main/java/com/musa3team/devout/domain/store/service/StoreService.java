package com.musa3team.devout.domain.store.service;

import com.musa3team.devout.common.constants.StoreStatus;
import com.musa3team.devout.common.constants.StoreCategory;
import com.musa3team.devout.domain.store.dto.PrepareResponseDto;
import com.musa3team.devout.domain.store.dto.StoreResponseDto;
import com.musa3team.devout.domain.store.entity.Store;
import com.musa3team.devout.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    @Transactional
    public StoreResponseDto save(String address, StoreCategory category, String name, String contents, Long minimumPrice, String telephoneNumber, LocalTime openTime, LocalTime closeTime) {
        Store store = new Store(
                telephoneNumber,
                address,
                contents,
                name,
                openTime,
                closeTime,
                minimumPrice,
                category
                );

        store.setStatus(StoreStatus.UNPREPARED);
        Store savedStore = storeRepository.save(store);

        return new StoreResponseDto(
                savedStore.getId(),
                savedStore.getTelephoneNumber(),
                savedStore.getAddress(),
                savedStore.getContents(),
                savedStore.getName(),
                savedStore.getOpenTime(),
                savedStore.getCloseTime(),
                savedStore.getMinimumPrice(),
                savedStore.getCategory()
        );
    }

    @Transactional
    @Scheduled(fixedRate = 600000) //10분마다 자동으로 폐업인 가게와 준비되지 않은 가게를 제외하고 영업상태를 변경해주는 메서드
    public void updateStatus() {
        LocalTime now = LocalTime.now();
        List<Store> stores = storeRepository.findAll();

        for (Store store : stores) {
            if(store.getStatus().equals(StoreStatus.UNPREPARED) || store.getStatus().equals(StoreStatus.SHUTDOWN)) continue;  //폐업이랑 미준비 가게는 제외
            updateStatusByTime(store, now);
        }
    }

    @Transactional
    public PrepareResponseDto prepare(Long id) {
        Store store = storeRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "요청하는 가게가 없거나 잘못 요청하셨습니다.")
        );

        store.setStatus(StoreStatus.CLOSE);
        LocalTime now = LocalTime.now();
        updateStatusByTime(store, now);

        return new PrepareResponseDto(
                store.getId(),
                store.getTelephoneNumber(),
                store.getAddress(),
                store.getContents(),
                store.getName(),
                store.getOpenTime(),
                store.getCloseTime(),
                store.getMinimumPrice(),
                store.getCategory(),
                store.getStatus()
        );
    }

    private void updateStatusByTime(Store store, LocalTime now) {
        if (store.getOpenTime().isBefore(store.getCloseTime())) { //openTime이 closeTime보다 이전에 있는 경우, 즉, opentime과 closetime이 같은 날에 있는 경우
            //opentime과 closetime이 같은 날인 경우
            if (now.isAfter(store.getOpenTime()) && now.isBefore(store.getCloseTime())) {
                store.setStatus(StoreStatus.OPEN);
            } else {
                store.setStatus(StoreStatus.CLOSE);
            }
        } else {
            //opentime이 오늘이고, closetime이 밤 12시 이상이 되어 다음날이 된 경우
            if (now.isAfter(store.getOpenTime()) || now.isBefore(store.getCloseTime())) {
                store.setStatus(StoreStatus.OPEN);
            } else {
                store.setStatus(StoreStatus.CLOSE);
            }
        }
    }
}
