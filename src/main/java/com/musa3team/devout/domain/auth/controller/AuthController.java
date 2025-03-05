package com.musa3team.devout.domain.auth.controller;

import com.musa3team.devout.domain.auth.dto.request.LoginRequest;
import com.musa3team.devout.domain.auth.dto.request.SignupRequest;
import com.musa3team.devout.domain.auth.dto.response.LoginResponse;
import com.musa3team.devout.domain.auth.dto.response.SignupResponse;
import com.musa3team.devout.domain.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest signupRequest, HttpServletResponse response) {

        SignupResponse signupResponse = authService.signup(signupRequest);

        response.setHeader("Authorization", signupResponse.getToken()); //발급받은 access토큰 헤더에 저장

        return new ResponseEntity<>(signupResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        LoginResponse loginResponse = authService.login(loginRequest);

        response.setHeader("Authorization", loginResponse.getToken());

        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }


}
