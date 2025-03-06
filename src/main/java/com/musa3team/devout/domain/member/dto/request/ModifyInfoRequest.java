package com.musa3team.devout.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class ModifyInfoRequest {

    @Schema(description = "닉네임", example = "테스트이름")
    private final String name;

    @Schema(description = "주소", example = "주소123")
    private final String address;

    @Schema(description = "번호", example = "010-1234-1234")
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
    @NotBlank(message = "전화번호는 필수 입력 값입니다.")
    private final String phoneNumber;

    public ModifyInfoRequest(String name, String address, String phoneNumber) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}
