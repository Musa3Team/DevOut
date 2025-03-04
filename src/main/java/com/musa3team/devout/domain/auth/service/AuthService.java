package com.musa3team.devout.domain.auth.service;

import com.musa3team.devout.common.config.PasswordEncoder;
import com.musa3team.devout.common.jwt.JwtUtil;
import com.musa3team.devout.domain.auth.dto.request.LoginRequest;
import com.musa3team.devout.domain.auth.dto.request.SignupRequest;
import com.musa3team.devout.domain.auth.dto.response.LoginResponse;
import com.musa3team.devout.domain.auth.dto.response.SignupResponse;
import com.musa3team.devout.domain.member.entity.Member;
import com.musa3team.devout.domain.member.repository.MemberRepository;
import com.sun.jdi.request.InvalidRequestStateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public SignupResponse signup(SignupRequest signupRequest) {

        if(memberRepository.existsByEmailAndMemberRole(signupRequest.getEmail(), signupRequest.getMemberRole())){
            throw new InvalidRequestStateException("해당 이메일로 가입된 사용자가 이미 존재합니다.");
        }

        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        Member newMember = new Member(
                signupRequest.getName(),
                signupRequest.getEmail(),
                encodedPassword,
                signupRequest.getAddress(),
                signupRequest.getPhoneNumber(),
                signupRequest.getMemberRole().name());

        Member signUpUser = memberRepository.save(newMember);

        String token = jwtUtil.createToken(signUpUser.getId(), signUpUser.getEmail(), signUpUser.getMemberRole());

        return new SignupResponse(signUpUser.getId(), signUpUser.getName(), signUpUser.getEmail(), signUpUser.getAddress(), signUpUser.getPhoneNumber(), signUpUser.getMemberRole(), token);
    }


    public LoginResponse login(LoginRequest loginRequest) {

        Member member = memberRepository.findByEmailAndMemberRole(loginRequest.getEmail(), loginRequest.getMemberRole()).orElseThrow(
                () -> new IllegalArgumentException("해당 이메일로 가입된 사용자가 없습니다.")
        );

        if(!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())){
            throw new InvalidRequestStateException("비밀번호가 틀렸습니다.");
        }

        String token = jwtUtil.createToken(member.getId(), member.getEmail(), member.getMemberRole());

        return new LoginResponse(token, member.getName());

    }
}
