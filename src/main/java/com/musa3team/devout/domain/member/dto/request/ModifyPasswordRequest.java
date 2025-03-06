package com.musa3team.devout.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ModifyPasswordRequest {

    @Schema(description = "기존번호", example = "test123!!")
    private String oldPassword;

    @Schema(description = "새번호", example = "newTest123!!")
    private String newPassword;

    public ModifyPasswordRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
