package com.musa3team.devout.domain.menu.controller;

import com.musa3team.devout.common.jwt.JwtUtil;
import com.musa3team.devout.domain.member.entity.MemberRole;
import com.musa3team.devout.domain.menu.dto.CreateMenuRequestDto;
import com.musa3team.devout.domain.menu.dto.UpdateMenuRequestDto;
import com.musa3team.devout.domain.menu.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Menu 관리 API", description = "메뉴 생성, 수정, 삭제 기능 API입니다.")
public class MenuController {

    private final MenuService menuService;
    private final JwtUtil jwtUtil;

    private ResponseEntity<?> validateOwner(HttpServletRequest request, Long storeId) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.info("Authorization 헤더가 없거나 잘못된 형식입니다.");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String token = jwtUtil.substringToken(authorizationHeader);
        Long tokenMemberId = jwtUtil.extractMemberId(token);
        MemberRole role = jwtUtil.extractMemberRole(token);

        if (role != MemberRole.OWNER || !menuService.isStoreOwner(tokenMemberId, storeId)) {
            log.info("권한 없음: 사장님이 아니거나 해당 가게의 소유자가 아닙니다.");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return null;
    }
    @Operation(summary = "메뉴 생성", description = "메뉴를 생성할 수 있습니다.")
    @PostMapping("/stores/{storeId}/menus")
    public ResponseEntity<?> save(@PathVariable Long storeId, @Valid @RequestBody CreateMenuRequestDto dto, HttpServletRequest request) {

        ResponseEntity<?> validationResult = validateOwner(request, storeId);
        if (validationResult != null) return validationResult;


        return new ResponseEntity<>(menuService.save(dto, storeId), HttpStatus.CREATED);
    }

    @Operation(summary = "메뉴 수정", description = "해당 메뉴의 가격, 이름, 설명 등을 수정할 수 있습니다.")
    @PutMapping("/stores/{storeId}/menus/{menuId}")
    public ResponseEntity<?> update(@PathVariable Long storeId, @PathVariable Long menuId, @RequestBody UpdateMenuRequestDto dto, HttpServletRequest request) {

        ResponseEntity<?> validationResult = validateOwner(request, storeId);
        if (validationResult != null) return validationResult;

        return new  ResponseEntity<>(menuService.update(storeId, menuId, dto), HttpStatus.OK);
    }

    @Operation(summary = "메뉴 삭제", description = "해당 메뉴를 삭제할 수 있습니다.")
    @DeleteMapping("/stores/{storeId}/menus/{menuId}")
    public ResponseEntity<?> delete(@PathVariable Long storeId, @PathVariable Long menuId, HttpServletRequest request) {

        ResponseEntity<?> validationResult = validateOwner(request, storeId);
        if (validationResult != null) return validationResult;

        menuService.disableById(storeId, menuId);

        return ResponseEntity.noContent().build();
    }
}
