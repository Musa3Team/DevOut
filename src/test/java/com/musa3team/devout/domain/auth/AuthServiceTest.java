package com.musa3team.devout.domain.auth;

import com.musa3team.devout.common.config.PasswordEncoder;
import com.musa3team.devout.common.jwt.JwtUtil;
import com.musa3team.devout.domain.auth.dto.request.SignupRequest;
import com.musa3team.devout.domain.auth.dto.response.SignupResponse;
import com.musa3team.devout.domain.auth.service.AuthService;
import com.musa3team.devout.domain.member.entity.Member;
import com.musa3team.devout.domain.member.entity.MemberRole;
import com.musa3team.devout.domain.member.repository.MemberRepository;
import com.musa3team.devout.domain.member.repository.TokenRepository;
import com.sun.jdi.request.InvalidRequestStateException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;


import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks //테스트 대상
    private AuthService authService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Test
    void 회원가입_성공(){
        //given
        Long memberId = 1L;
        SignupRequest request = new SignupRequest(
                "이름테스트",
                "test@naver.com",
                "Qlalfqjsgh33!",
                "서울시어쩌구",
                "010-1234-1234",
                MemberRole.CUSTOMER);

        given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");

        Member mockMember = new Member(
                request.getName(),
                request.getEmail(),
                "encodedPassword",
                request.getAddress(),
                request.getPhoneNumber(),
                request.getMemberRole().name()
        );

        ReflectionTestUtils.setField(mockMember, "id", memberId);

        given(memberRepository.save(any(Member.class))).willReturn(mockMember);

        given(jwtUtil.createAccessToken(anyLong(), anyString(), any())).willReturn("mockToken");

        given(tokenRepository.findByEmail(anyString())).willReturn(Optional.empty());

        //when
        SignupResponse signupResponse = authService.signup(request);

        //then
        assertNotNull(signupResponse);
        assertNotNull(signupResponse.getId());
        assertEquals(memberId, signupResponse.getId());

    }

    @Test
    void 이미_존재하는_회원이_회원가입_시_InvalidRequestException을_던진다(){
        // given
        SignupRequest request = new SignupRequest(
                "이름테스트",
                "test@naver.com",
                "Qlalfqjsgh33!",
                "서울시어쩌구",
                "010-1234-1234",
                MemberRole.CUSTOMER);

        given(memberRepository.findByEmailAndMemberRole(request.getEmail(), request.getMemberRole())).willReturn(Optional.of(new Member()));

        //when & then
        assertThrows(InvalidRequestStateException.class,
                () -> authService.signup(request)
        );

    }
}
