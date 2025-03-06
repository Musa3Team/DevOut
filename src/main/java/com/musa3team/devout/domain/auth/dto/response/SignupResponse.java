package com.musa3team.devout.domain.auth.dto.response;

import com.musa3team.devout.domain.member.entity.Member;
import com.musa3team.devout.domain.member.entity.MemberRole;
import lombok.Getter;

@Getter
public class SignupResponse {
    private final Long id;
    private final String name;
    private final String email;
    private final String address;
    private final String phoneNumber;
    private final MemberRole memberRole;

    private final String token;

    public SignupResponse(Long id, String name, String email, String address, String phoneNumber, MemberRole memberRole, String token) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.memberRole = memberRole;
        this.token = token;
    }

    public static SignupResponse toDto(Member member, String token) {
        return new SignupResponse(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getAddress(),
                member.getPhoneNumber(),
                member.getMemberRole(),
                token
        );
    }
}
