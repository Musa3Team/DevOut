package com.musa3team.devout.domain.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class DeleteRequestDto {

    @Schema(description = "비밀번호", example = "test123!!")
    private String password;
}
