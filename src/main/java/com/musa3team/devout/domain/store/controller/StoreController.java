package com.musa3team.devout.domain.store.controller;

import com.musa3team.devout.common.constants.StoreCategory;
import com.musa3team.devout.common.jwt.JwtUtil;
import com.musa3team.devout.domain.member.entity.MemberRole;
import com.musa3team.devout.domain.store.dto.*;
import com.musa3team.devout.domain.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
public class StoreController {

    private final StoreService storeService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<StoreResponseDto> save(@RequestBody @Valid StoreRequestDto requestDto, @RequestHeader("Authorization") String token) {
        String substringToken = jwtUtil.substringToken(token);
        MemberRole memberRole = jwtUtil.extractMemberRole(substringToken);

        if(memberRole.equals(MemberRole.CUSTOMER))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "사장님만 가능합니다.");

        Long memberId = jwtUtil.extractMemberId(substringToken);

        StoreResponseDto store = storeService.save(
                memberId,
                requestDto.getAddress(),
                requestDto.getCategory(),
                requestDto.getName(),
                requestDto.getContents(),
                requestDto.getMinimum_price(),
                requestDto.getTelephone_number(),
                requestDto.getOpen_time(),
                requestDto.getClose_time()
        );
        return new ResponseEntity<>(store, HttpStatus.CREATED);
    }

    @PatchMapping("/status/{id}")
    public ResponseEntity<StoreResponseDto> SetStatusToPrepareOrUnprepared(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        String substringToken = jwtUtil.substringToken(token);
        MemberRole memberRole = jwtUtil.extractMemberRole(substringToken);

        if(memberRole.equals(MemberRole.CUSTOMER))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "사장님만 가능합니다.");

        StoreResponseDto prepare = storeService.prepare(id);
        return new ResponseEntity<>(prepare, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<StoreResponseDto> update(
            @PathVariable Long id,
            @RequestBody @Valid StoreUpdateRequestDto requestDto,
            @RequestHeader("Authorization") String token
    ) {
        String substringToken = jwtUtil.substringToken(token);
        MemberRole memberRole = jwtUtil.extractMemberRole(substringToken);

        if(memberRole.equals(MemberRole.CUSTOMER))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "사장님만 가능합니다.");

        StoreResponseDto update = storeService.update(
                id,
                requestDto.getAddress(),
                requestDto.getCategory(),
                requestDto.getName(),
                requestDto.getContents(),
                requestDto.getMinimum_price(),
                requestDto.getTelephone_number(),
                requestDto.getOpen_time(),
                requestDto.getClose_time()
        );

        return new ResponseEntity<>(update, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FindByIdResponseDto> findById(@PathVariable Long id) {
        FindByIdResponseDto findStore = storeService.findById(id);
        return new ResponseEntity<>(findStore, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<StorePageResponseDto> findByAllPage(
            @RequestParam (defaultValue = "1") int page,
            @RequestParam (defaultValue = "10") int pageSize,
            @RequestParam String name,
            @RequestParam StoreCategory category
    ) {
        StorePageResponseDto stores = storeService.findAll(page, pageSize, name, category);
        return new ResponseEntity<>(stores, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token,
            @RequestBody DeleteRequestDto requestDto
    ) {
        String substringToken = jwtUtil.substringToken(token);
        MemberRole memberRole = jwtUtil.extractMemberRole(substringToken);

        if(memberRole.equals(MemberRole.CUSTOMER))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "사장님만 가능합니다.");

        Long memberId = jwtUtil.extractMemberId(substringToken);

        storeService.delete(id, memberId, requestDto.getPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
