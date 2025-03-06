package com.musa3team.devout.domain.auth.dto.request;

import com.musa3team.devout.domain.member.entity.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class LoginRequest {

    @Schema(description = "이메일", example = "email@email.com")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;
    @Schema(description = "비밀번호", example = "test123!!")
    private String password;
    @Schema(description = "유저상태", example = "CUSTOMER")
    private MemberRole memberRole;

}
