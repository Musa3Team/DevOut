package com.musa3team.devout.domain.member.dto.response;

import lombok.Getter;

@Getter
public class MemberResponse {

    private final Long id;
    private final String name;
    private final String email;
    private final String address;
    private final String phoneNumber;

    public MemberResponse(Long id, String name, String email, String address, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;

    }
}
