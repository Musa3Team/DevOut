package com.musa3team.devout.domain.menu.service;

import com.musa3team.devout.domain.menu.dto.CreateMenuRequestDto;
import com.musa3team.devout.domain.menu.dto.MenuResponseDto;
import com.musa3team.devout.domain.menu.dto.UpdateMenuRequestDto;
import com.musa3team.devout.domain.menu.entity.Menu;
import com.musa3team.devout.domain.menu.entity.MenuCategory;
import com.musa3team.devout.domain.menu.repository.MenuRepository;
import com.musa3team.devout.domain.store.entity.Store;
import com.musa3team.devout.domain.store.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private MenuService menuService;

    @Test
    void Menu를_정상적으로_등록한다(){

        // given
        Long storeId = 1L;
        Store store = new Store();

        String name = "탕수육";
        int price = 12000;
        String contents = "맛있는 탕수육";
        MenuCategory category = MenuCategory.MAIN_DISHES;

        CreateMenuRequestDto request = new CreateMenuRequestDto(name, price, contents, category);

        Menu savedMenu = new Menu(store, request.getName(), request.getPrice(), request.getContents(), request.getCategory());

        given(storeRepository.findById(storeId)).willReturn(Optional.of(store));
        given(menuRepository.save(any(Menu.class))).willReturn(savedMenu);

        // when

        MenuResponseDto result = menuService.save(request, storeId);

        //then

        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(price, result.getPrice());
        assertEquals(contents, result.getContents());
        assertEquals(category, result.getCategory());
    }

    @Test
    void Menu를_업데이트할_수_있다() {

        // given

        Long storeId = 1L;
        Long menuId = 1L;
        Store store = new Store();

        Menu oldMenu = new Menu(store, "탕수육", 12000, "맛있어", MenuCategory.MAIN_DISHES);

        UpdateMenuRequestDto request = new UpdateMenuRequestDto("짜장면", 5000, "아주 맛있어", MenuCategory.MAIN_DISHES);

        given(storeRepository.findById(storeId)).willReturn(Optional.of(store));
        given(menuRepository.findByIdAndStore(menuId, store)).willReturn(Optional.of(oldMenu));

        // when

        MenuResponseDto result = menuService.update(storeId, menuId, request);

        // then

        assertNotNull(result);
        assertEquals("짜장면", result.getName());
        assertEquals(5000, result.getPrice());
        assertEquals("아주 맛있어", result.getContents());
        assertEquals(MenuCategory.MAIN_DISHES, result.getCategory());

    }

    @Test
    void Menu를_비활성할_수_있다() {

        // given

        Long storeId = 1L;
        Long menuId = 1L;
        Store store = new Store();
        ReflectionTestUtils.setField(store, "id", storeId);

        Menu savedMenu = new Menu(store, "탕수육", 12000, "맛있어", MenuCategory.MAIN_DISHES);

        given(storeRepository.findById(storeId)).willReturn(Optional.of(store));
        given(menuRepository.findByIdAndStore(menuId, store)).willReturn(Optional.of(savedMenu));

        // when

        menuService.disableById(storeId, menuId);

        // then

        verify(menuRepository).save(savedMenu);
    }
}