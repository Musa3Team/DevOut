package com.musa3team.devout.domain.store.service;

import com.musa3team.devout.domain.store.category.Category;
import com.musa3team.devout.domain.store.dto.StoreResponseDto;
import com.musa3team.devout.domain.store.entity.Store;
import com.musa3team.devout.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    @Transactional
    public StoreResponseDto save(String address, Category category, String name, String contents, Long minimumPrice, String telephoneNumber, LocalTime openTime, LocalTime closeTime) {
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

        Store savedStore = storeRepository.save(store);

        return new StoreResponseDto(
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
}
