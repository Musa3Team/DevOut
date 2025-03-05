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
import org.springframework.data.domain.*;
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

        if(store.getStatus().equals(StoreStatus.SHUTDOWN))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "폐업한 가게입니다.");

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

        if(store.getStatus().equals(StoreStatus.SHUTDOWN))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "폐업한 가게입니다.");

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

    @Transactional(readOnly = true)
    public FindByIdResponseDto findById(Long id) {
        Store store = storeRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "가게가 존재하지 않거나 잘못입력했습니다.")
        );

        if(store.getStatus().equals(StoreStatus.SHUTDOWN))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "폐업한 가게입니다.");

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

    @Transactional(readOnly = true)
    public StorePageResponseDto findAll(int page, int pageSize, String name, StoreCategory category) {

        List<Store> storeList = storeRepository.findAllByNameAndCategory(name, category);
        List<Store> filteredStores = storeList.stream()
                .filter(store -> !store.getStatus().equals(StoreStatus.SHUTDOWN))
                .toList();  //페이징처리하기전에 폐업한 가게 필터링을 먼저해야함

        int pageNum = (page > 0) ? page - 1 : 0;    //사람한테는 1이 시작이지만 jpa가 제공하는 page는 시작 번호는 0번부터임
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.ASC, "minimumPrice"));

        int totalElements = filteredStores.size();  //전부 몇개인지
        int start = (int) pageable.getOffset(); //시작 번호
        int end = Math.min((start + pageable.getPageSize()), totalElements);    //끝 번호
        List<Store> pageContent = filteredStores.subList(start, end);   //filteredStores 리스트에서 start 인덱스부터 end 인덱스까지의 부분 리스트를 반환. subList()는 end 인덱스 요소는 포함 안하는게 java의 관행임

        Page<Store> storePage = new PageImpl<>(pageContent, pageable, totalElements);   //여기서 필터링된 가게들을 페이징 처리함
        int currentPage = storePage.getNumber() + 1;    //현재 페이지번호
        int totalPages = storePage.getTotalPages();     //총 페이지 번호

        List<StoreResponseDto> stores = storePage.stream()
                .map(store -> new StoreResponseDto(
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
                )).toList();    //storePage 객체를 stream.map 으로 List<StoreResponseDto> 객체로 변환

        return new StorePageResponseDto(stores, currentPage, totalPages, totalElements);
    }

    @Transactional
    public void delete(Long id) {
        Store store = storeRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않거나 잘못 입력했습니다.")
        );
        store.setStatus(StoreStatus.SHUTDOWN);
    }
}
