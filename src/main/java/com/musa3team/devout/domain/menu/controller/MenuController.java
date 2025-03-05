package com.musa3team.devout.domain.menu.controller;

import com.musa3team.devout.common.jwt.JwtUtil;
import com.musa3team.devout.domain.member.entity.MemberRole;
import com.musa3team.devout.domain.menu.dto.CreateMenuRequestDto;
import com.musa3team.devout.domain.menu.dto.UpdateMenuRequestDto;
import com.musa3team.devout.domain.menu.service.MenuService;
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

    @PostMapping("/stores/{storeId}/menus")
    public ResponseEntity<?> save(@PathVariable Long storeId, @Valid @RequestBody CreateMenuRequestDto dto, HttpServletRequest request) {

        ResponseEntity<?> validationResult = validateOwner(request, storeId);
        if (validationResult != null) return validationResult;


        return new ResponseEntity<>(menuService.save(dto, storeId), HttpStatus.CREATED);
    }

    @PatchMapping("/stores/{storeId}/menus/{menuId}")
    public ResponseEntity<?> update(@PathVariable Long storeId, @PathVariable Long menuId, @RequestBody UpdateMenuRequestDto dto, HttpServletRequest request) {

        ResponseEntity<?> validationResult = validateOwner(request, storeId);
        if (validationResult != null) return validationResult;

        return new  ResponseEntity<>(menuService.update(storeId, menuId, dto), HttpStatus.OK);
    }

    @DeleteMapping("/stores/{storeId}/menus/{menuId}")
    public ResponseEntity<?> delete(@PathVariable Long storeId, @PathVariable Long menuId, HttpServletRequest request) {

        ResponseEntity<?> validationResult = validateOwner(request, storeId);
        if (validationResult != null) return validationResult;

        menuService.disableById(storeId, menuId);

        return ResponseEntity.noContent().build();
    }
}
