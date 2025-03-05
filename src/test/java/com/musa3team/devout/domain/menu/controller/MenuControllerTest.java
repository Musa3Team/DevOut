package com.musa3team.devout.domain.menu.controller;

import com.musa3team.devout.domain.menu.dto.CreateMenuRequestDto;
import com.musa3team.devout.domain.menu.dto.MenuResponseDto;
import com.musa3team.devout.domain.menu.entity.MenuCategory;
import com.musa3team.devout.domain.menu.entity.MenuStatus;
import com.musa3team.devout.domain.menu.service.MenuService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(MenuController.class)
class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MenuService menuService;

    @Test
    void Menu_생성() throws Exception {
        //given
        long storeId = 1L;
        CreateMenuRequestDto requestDto = new CreateMenuRequestDto();
        requestDto.setName("탕수육");
        requestDto.setPrice(12000);
        requestDto.setContents("맛있는 탕수육");
        requestDto.setCategory(MenuCategory.MAIN_DISHES);

        MenuResponseDto responseDto = new MenuResponseDto(1L, "탕수육", 12000, "맛있는 탕수육", MenuStatus.ACTIVE, MenuCategory.MAIN_DISHES);

        // when


        // then

    }
}