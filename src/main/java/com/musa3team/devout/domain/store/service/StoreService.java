package com.musa3team.devout.domain.store.service;

import com.musa3team.devout.common.constants.StoreStatus;
import com.musa3team.devout.common.constants.StoreCategory;
import com.musa3team.devout.domain.menu.dto.MenuResponseDto;
import com.musa3team.devout.domain.menu.entity.Menu;
import com.musa3team.devout.domain.menu.repository.MenuRepository;
import com.musa3team.devout.domain.store.dto.FindByIdResponseDto;
import com.musa3team.devout.domain.store.dto.StorePageResponseDto;
import com.musa3team.devout.domain.store.dto.StoreResponseDto;
import com.musa3team.devout.domain.store.entity.Store;
import com.musa3team.devout.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;
import java.util.List;
import java.util.regex.Pattern;

import static com.musa3team.devout.domain.store.valid.TenMinuteIntervalValidator.isValidTenMinuteInterval;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;

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
                savedStore.getCategory(),
                store.getStatus()
        );
    }

    @Transactional
    @Scheduled(fixedRate = 600000) //10분마다 자동으로 폐업인 가게와 준비되지 않은 가게를 제외하고 영업상태를 변경해주는 메서드
    public void updateStatus() {
        LocalTime now = LocalTime.now();
        List<Store> stores = storeRepository.findAll();

        for (Store store : stores) {
            if (store.getStatus().equals(StoreStatus.UNPREPARED) || store.getStatus().equals(StoreStatus.SHUTDOWN))
                continue;  //폐업이랑 미준비 가게는 제외
            updateStatusByTime(store, now);
        }
    }

    @Transactional
    public StoreResponseDto prepare(Long id) {
        Store store = storeRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "요청하는 가게가 없거나 잘못 요청하셨습니다.")
        );

        if (store.getStatus().equals(StoreStatus.UNPREPARED)) store.setStatus(StoreStatus.CLOSE);
        else {
            store.setStatus(StoreStatus.UNPREPARED);
            return new StoreResponseDto(
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
        LocalTime now = LocalTime.now();
        updateStatusByTime(store, now);

        return new StoreResponseDto(
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

    @Transactional
    public StoreResponseDto update(Long id, String address, StoreCategory category, String name, String contents, Long minimumPrice, String telephoneNumber, LocalTime openTime, LocalTime closeTime) {
        Store store = storeRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "잘못 입력했거나 존재하지 않습니다.")
        );

        if (telephoneNumber != null && !address.isBlank()) store.setAddress(address);
        if (category != null) store.setCategory(category);
        if (name != null && !name.isBlank()) store.setName(name);
        if (contents != null && contents.isBlank()) store.setContents(contents);
        if (minimumPrice != null) store.setMinimumPrice(minimumPrice);
        if (telephoneNumber != null && !telephoneNumber.isBlank()) {
            if (Pattern.matches("^(02|0[3-6][1-5])-?\\d{3,4}-?\\d{4}$", telephoneNumber))
                store.setTelephoneNumber(telephoneNumber);
            else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "전화번호 형식을 확인하세요");
        }
        if (openTime != null) {
            if (isValidTenMinuteInterval(openTime)) store.setOpenTime(openTime);
            else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "분은 10분 단위만 입력 가능합니다.");
        }
        if (closeTime != null) {
            if (isValidTenMinuteInterval(closeTime)) store.setCloseTime(closeTime);
            else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "분은 10분 단위만 입력 가능합니다.");
        }

        return new StoreResponseDto(
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

    public FindByIdResponseDto findById(Long id) {
        Store store = storeRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "가게가 존재하지 않거나 잘못입력했습니다.")
        );

        List<Menu> menus = menuRepository.findAllByStoreId(id);

        return new FindByIdResponseDto(
                store.getId(),
                store.getTelephoneNumber(),
                store.getAddress(),
                store.getContents(),
                store.getName(),
                store.getOpenTime(),
                store.getCloseTime(),
                store.getMinimumPrice(),
                store.getCategory(),
                store.getStatus(),
                menus.stream().map(
                        menu -> new MenuResponseDto(
                                menu.getId(),
                                menu.getName(),
                                menu.getContents(),
                                menu.getPrice()
                        )
                ).toList()
        );
    }

    public StorePageResponseDto findAll(int page, int pageSize, String name, StoreCategory category) {
        int pageNum = (page > 0) ? page - 1 : 0;
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.ASC, "minimumPrice"));
        Page<Store> storePage = storeRepository.findAllByNameAndCategory(name, category, pageable);
        int currentPage = storePage.getNumber() + 1;
        int totalPages = storePage.getTotalPages();
        long totalElements = storePage.getTotalElements();

        List<StoreResponseDto> stores = storePage.stream().map(
                store -> new StoreResponseDto(
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
                )
        ).toList();

        return new StorePageResponseDto(stores, currentPage, totalPages, totalElements);
    }
}
