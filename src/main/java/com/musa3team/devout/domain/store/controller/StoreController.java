package com.musa3team.devout.domain.store.controller;

import com.musa3team.devout.common.constants.StoreCategory;
import com.musa3team.devout.common.jwt.JwtUtil;
import com.musa3team.devout.domain.member.entity.MemberRole;
import com.musa3team.devout.domain.store.dto.*;
import com.musa3team.devout.domain.store.service.StoreService;
import jakarta.servlet.http.HttpServletRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
@Tag(name = "Store 관리 API", description = "사장 유저가 가게 생성, 폐업, 조회기능 API입니다.")
public class StoreController {

    private final StoreService storeService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "가게 생성", description = "사장 유저가 가게를 생성할 수 있습니다.")
    @PostMapping
    public ResponseEntity<StoreResponseDto> save(@RequestBody @Valid StoreRequestDto requestDto, HttpServletRequest request) {
        String substringToken = getSubstringToken(request);
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

    @Operation(summary = "가게 상태 수정", description = "사장 유저가 가게 상태를 수정할 수 있습니다.")
    @PatchMapping("/status/{id}")
    public ResponseEntity<StoreResponseDto> SetStatusToPrepareOrUnprepared(@PathVariable Long id, HttpServletRequest request) {
        String substringToken = getSubstringToken(request);
        MemberRole memberRole = jwtUtil.extractMemberRole(substringToken);

        if(memberRole.equals(MemberRole.CUSTOMER))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "사장님만 가능합니다.");

        StoreResponseDto prepare = storeService.prepare(id);
        return new ResponseEntity<>(prepare, HttpStatus.OK);
    }

    @Operation(summary = "가게 정보 수정", description = "사장 유저가 가게 정보를 수정할 수 있습니다.")
    @PatchMapping("/{id}")
    public ResponseEntity<StoreResponseDto> update(
            @PathVariable Long id,
            @RequestBody @Valid StoreUpdateRequestDto requestDto,
            HttpServletRequest request
    ) {
        String substringToken = getSubstringToken(request);
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

    @Operation(summary = "가게 단건 조회", description = "가게 정보를 조회할 수 있습니다.")
    @GetMapping("/{id}")
    public ResponseEntity<FindByIdResponseDto> findById(@PathVariable Long id) {
        FindByIdResponseDto findStore = storeService.findById(id);
        return new ResponseEntity<>(findStore, HttpStatus.OK);
    }

    @Operation(summary = "가게 다건 조회", description = "가게 정보를 가게명 기준으로 조회할 수 있습니다.")
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
            HttpServletRequest request,
            @RequestBody DeleteRequestDto requestDto
    ) {
        String substringToken = getSubstringToken(request);
        MemberRole memberRole = jwtUtil.extractMemberRole(substringToken);

        if(memberRole.equals(MemberRole.CUSTOMER))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "사장님만 가능합니다.");

        Long memberId = jwtUtil.extractMemberId(substringToken);

        storeService.delete(id, memberId, requestDto.getPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private String getSubstringToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return jwtUtil.substringToken(token);
    }
}
