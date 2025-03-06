package com.musa3team.devout.domain.store;

import com.musa3team.devout.common.constants.StoreCategory;
import com.musa3team.devout.common.constants.StoreStatus;
import com.musa3team.devout.common.jwt.JwtUtil;
import com.musa3team.devout.domain.store.controller.StoreController;
import com.musa3team.devout.domain.store.dto.FindByIdResponseDto;
import com.musa3team.devout.domain.store.service.StoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(StoreController.class)
public class StoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StoreService storeService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Test
    void Store_단건조회() throws Exception{
        //given
        Long id = 1L;

        given(storeService.findById(id)).willReturn(new FindByIdResponseDto(
                id,
                "032-000-0000",
                "지구밖",
                "ccccc",
                "name",
                LocalTime.of(10,0),
                LocalTime.of(22,0),
                20000,
                StoreCategory.CHICKEN,
                StoreStatus.OPEN,
                null
        ));

        //when & then
        mockMvc.perform(get("/stores/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }
}
