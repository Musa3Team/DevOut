package com.musa3team.devout.domain.member.dto.response;

import lombok.Getter;

@Getter
public class MemberResponse {

    private final Long id;
    private final String name;
    private final String email;
    private final String phoneNumber;
    private final String address;


    public MemberResponse(Long id, String name, String email, String phoneNumber, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
}
