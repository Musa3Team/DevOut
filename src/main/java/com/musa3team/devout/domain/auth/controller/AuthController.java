package com.musa3team.devout.domain.auth.controller;

import com.musa3team.devout.domain.auth.dto.request.LoginRequest;
import com.musa3team.devout.domain.auth.dto.request.SignupRequest;
import com.musa3team.devout.domain.auth.dto.response.LoginResponse;
import com.musa3team.devout.domain.auth.dto.response.SignupResponse;
import com.musa3team.devout.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Auth API", description = "회원가입과 로그인 기능 API 입니다.")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "회원가입 시 토큰이 발급됩니다.")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest signupRequest, HttpServletResponse response) {

        SignupResponse signupResponse = authService.signup(signupRequest);

        response.setHeader("Authorization", signupResponse.getToken()); //발급받은 access토큰 헤더에 저장

        return new ResponseEntity<>(signupResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인 시 토큰이 발급됩니다.")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        LoginResponse loginResponse = authService.login(loginRequest);

        response.setHeader("Authorization", loginResponse.getToken());

        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }


}
