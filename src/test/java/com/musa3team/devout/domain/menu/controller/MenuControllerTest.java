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
}