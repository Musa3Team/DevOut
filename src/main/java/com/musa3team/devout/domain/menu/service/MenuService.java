package com.musa3team.devout.domain.menu.service;

import com.musa3team.devout.domain.menu.dto.CreateMenuRequestDto;
import com.musa3team.devout.domain.menu.dto.MenuResponseDto;
import com.musa3team.devout.domain.menu.dto.UpdateMenuRequestDto;
import com.musa3team.devout.domain.menu.entity.Menu;
import com.musa3team.devout.domain.menu.repository.MenuRepository;
import com.musa3team.devout.domain.store.entity.Store;
import com.musa3team.devout.domain.store.repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public MenuResponseDto save(CreateMenuRequestDto dto, Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new EntityNotFoundException("해당 가게가 없습니다."));
        Menu menu = new Menu(store, dto.getName(), dto.getPrice(), dto.getContents(), dto.getCategory());
        Menu savedMenu = menuRepository.save(menu);
        return new MenuResponseDto(savedMenu.getId(), savedMenu.getName(), savedMenu.getPrice(), savedMenu.getContents(), savedMenu.getStatus(), savedMenu.getCategory());
    }

    @Transactional
    public MenuResponseDto update(Long storeId, Long menuId, UpdateMenuRequestDto dto) {

        Store store = storeRepository.findById(storeId).orElseThrow(() -> new EntityNotFoundException("해당 가게가 없습니다."));

        Menu menu = menuRepository.findByIdAndStore(menuId, store).orElseThrow(() -> new EntityNotFoundException("해당 메뉴가 없거나 해당 가게의 메뉴가 아닙니다."));
        menu.update(dto.getName(), dto.getPrice(), dto.getContents(), dto.getCategory());

        return new MenuResponseDto(menu.getId(), menu.getName(), menu.getPrice(), menu.getContents(), menu.getStatus(), menu.getCategory());
    }

    @Transactional
    public void disableById(Long storeId, Long menuId) {

        Store store = storeRepository.findById(storeId).orElseThrow(() -> new EntityNotFoundException("해당 가게가 없습니다."));

        Menu menu = menuRepository.findByIdAndStore(menuId, store).orElseThrow(() -> new EntityNotFoundException("해당 메뉴가 없거나 해당 가게의 메뉴가 아닙니다."));
        menu.disable();
        menuRepository.save(menu);
    }

    public boolean isStoreOwner(Long memberId, Long storeId) {

        return storeRepository.findByIdAndOwnerId(storeId, memberId).isPresent();
    }
}
