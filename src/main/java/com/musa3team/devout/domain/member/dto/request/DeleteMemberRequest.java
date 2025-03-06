package com.musa3team.devout.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class DeleteMemberRequest {
    @Schema(description = "비밀번호", example = "test123!")
    private String password;

}
