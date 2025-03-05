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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

        Optional<Member> existingMember = memberRepository.findByEmailAndMemberRole(signupRequest.getEmail(), signupRequest.getMemberRole());

        if(existingMember.isPresent()) {
            Member member = existingMember.get();

            if(member.getDeletedAt() != null){
                throw new InvalidRequestStateException("탈퇴한 이메일은 재사용이 불가능합니다.");
            }

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

        if(member.getDeletedAt() != null){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "탈퇴된 회원입니다.");
        }

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

        String newRefreshToken = jwtUtil.createRefreshToken(member.getId(), member.getEmail(), member.getMemberRole());

        if (existingToken.isPresent()) {
            // 기존 토큰이 있으면 updateToken 메서드로 새로운 refreshToken 저장
            RefreshToken refreshTokenEntity = existingToken.get();
            refreshTokenEntity.updateToken(newRefreshToken);
            tokenRepository.save(refreshTokenEntity);
        } else {
            // 기존 토큰이 없으면 새로 생성하여 저장
            RefreshToken newTokenEntity = new RefreshToken(newRefreshToken, member.getEmail());
            tokenRepository.save(newTokenEntity);
        }
        return newRefreshToken;
    }
}
