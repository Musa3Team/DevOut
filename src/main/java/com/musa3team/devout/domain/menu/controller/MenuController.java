package com.musa3team.devout.domain.menu.controller;

import com.musa3team.devout.domain.menu.dto.CreateMenuRequestDto;
import com.musa3team.devout.domain.menu.dto.MenuResponseDto;
import com.musa3team.devout.domain.menu.dto.UpdateMenuRequestDto;
import com.musa3team.devout.domain.menu.entity.Menu;
import com.musa3team.devout.domain.menu.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PostMapping("/stores/{storeId}/menus")
    public ResponseEntity<MenuResponseDto> save(@PathVariable Long storeId, @Valid @RequestBody CreateMenuRequestDto dto) {

        return new ResponseEntity<>(menuService.save(dto, storeId), HttpStatus.CREATED);
    }

    @PatchMapping("/stores/{storeId}/menus/{menuId}")
    public ResponseEntity<MenuResponseDto> update(@PathVariable Long storeId, @PathVariable Long menuId, @RequestBody UpdateMenuRequestDto dto) {

        return new  ResponseEntity<>(menuService.update(storeId, menuId, dto), HttpStatus.OK);
    }

    @DeleteMapping("/stores/{storeId}/menus/{menuId}")
    public ResponseEntity<Void> delete(@PathVariable Long storeId, @PathVariable Long menuId) {

        menuService.disableById(storeId, menuId);

        return ResponseEntity.noContent().build();
    }
}
