package com.musa3team.devout.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class LoginResponse {

    private final String token;
    private final String username;

    public LoginResponse(String token, String username) {
        this.token = token;
        this.username = username;
    }
}
