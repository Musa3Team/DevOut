package com.musa3team.devout.domain.auth.service;

import com.musa3team.devout.common.config.PasswordEncoder;
import com.musa3team.devout.common.jwt.JwtUtil;
import com.musa3team.devout.domain.auth.dto.request.LoginRequest;
import com.musa3team.devout.domain.auth.dto.request.SignupRequest;
import com.musa3team.devout.domain.auth.dto.response.LoginResponse;
import com.musa3team.devout.domain.auth.dto.response.SignupResponse;
import com.musa3team.devout.domain.member.entity.Member;
import com.musa3team.devout.domain.member.entity.RefreshToken;
import com.musa3team.devout.domain.member.repository.MemberRepository;
import com.musa3team.devout.domain.member.repository.TokenRepository;
import com.sun.jdi.request.InvalidRequestStateException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
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

        Member member = memberRepository.save(newMember);

        String accessToken = jwtUtil.createAccessToken(member.getId(), member.getEmail(), member.getMemberRole());
        createOrUpdateRefreshToken(member);

        return new SignupResponse(member.getId(), member.getName(), member.getEmail(), member.getAddress(), member.getPhoneNumber(), member.getMemberRole(), accessToken);
    }


    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {

        Member member = memberRepository.findByEmailAndMemberRole(loginRequest.getEmail(), loginRequest.getMemberRole()).orElseThrow(
                () -> new IllegalArgumentException("해당 이메일로 가입된 사용자가 없습니다.")
        );

        if(!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())){
            throw new InvalidRequestStateException("비밀번호가 틀렸습니다.");
        }

        String accessToken = jwtUtil.createAccessToken(member.getId(), member.getEmail(), member.getMemberRole());
        createOrUpdateRefreshToken(member);


        return new LoginResponse(accessToken, member.getName());

    }

    private String createOrUpdateRefreshToken(Member member){
        Optional<RefreshToken> existingToken = tokenRepository.findByEmail(member.getEmail());

        // 기존 리프레시 토큰이 있으면 만료 시간을 확인하고, 만료되지 않았다면 재사용
        if (existingToken.isPresent() && !jwtUtil.isTokenExpired(existingToken.get().getRefreshToken())) {
            return existingToken.get().getRefreshToken();
        }

        // 리프레시 토큰이 없거나 만료된 경우 새로운 리프레시 토큰 생성
        String newRefreshToken = jwtUtil.createRefreshToken(member.getId(), member.getEmail(), member.getMemberRole());
        tokenRepository.save(new RefreshToken(newRefreshToken, member.getEmail()));
        return newRefreshToken;
    }
}
