package com.musa3team.devout.domain.member.dto.request;

import lombok.Getter;

@Getter
public class ModifyPasswordRequest {

    private String oldPassword;
    private String newPassword;

    public ModifyPasswordRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
