package com.musa3team.devout.domain.store.controller;

import com.musa3team.devout.domain.store.dto.PrepareResponseDto;
import com.musa3team.devout.domain.store.dto.StoreRequestDto;
import com.musa3team.devout.domain.store.dto.StoreResponseDto;
import com.musa3team.devout.domain.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<StoreResponseDto> save(@RequestBody @Valid StoreRequestDto requestDto) {
        StoreResponseDto store = storeService.save(
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

    @PatchMapping("/{id}")
    public ResponseEntity<PrepareResponseDto> SetStatusToPrepare(@PathVariable Long id) {
        PrepareResponseDto prepare = storeService.prepare(id);
        return new ResponseEntity<>(prepare, HttpStatus.OK);
    }
}
