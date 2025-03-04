package com.musa3team.devout.domain.auth.dto.request;

import com.musa3team.devout.domain.member.entity.MemberRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class LoginRequest {

    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private final String email;
    private final String password;
    private final MemberRole memberRole;

    public LoginRequest(String email, String password, MemberRole memberRole) {
        this.email = email;
        this.password = password;
        this.memberRole = memberRole;
    }

}
