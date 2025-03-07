package com.musa3team.devout.common.jwt;

import com.musa3team.devout.domain.member.entity.MemberRole;
import com.musa3team.devout.domain.member.entity.RefreshToken;
import com.musa3team.devout.domain.member.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class JwtFilterTest {

    @InjectMocks
    private JwtFilter jwtFilter;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private TokenRepository tokenRepository;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = mock(FilterChain.class);

    }

    @Test
    void 화이트리스트_요청_필터_통과_성공() throws ServletException, IOException {

        // given
        request.setRequestURI("/auth/login");

        // when
        jwtFilter.doFilter(request, response, filterChain);

        //then
        verify(filterChain, times(1)).doFilter(request, response); //doFilter가 한번 실행되는지 확인
    }


    @Test
    void 엑세스토큰_유효_검증_성공() throws ServletException, IOException {
        //given
        request.setRequestURI("/endpoint");
        request.addHeader("Authorization", "Bearer valid_access_token");

        Claims claims = mock(Claims.class);
        when(jwtUtil.extractClaims("valid_access_token")).thenReturn(claims); // 토큰 파싱
        when(claims.getSubject()).thenReturn("1"); //id
        when(claims.get("email", String.class)).thenReturn("test@naver.com");
        when(claims.get("memberRole", String.class)).thenReturn(MemberRole.CUSTOMER.toString());
        when(claims.getExpiration()).thenReturn(new Date(System.currentTimeMillis()+10000));

        //when
        jwtFilter.doFilter(request, response, filterChain);

        //then
        assertEquals("1", request.getAttribute("memberId").toString());
        assertEquals("test@naver.com", request.getAttribute("email"));
        assertEquals(MemberRole.CUSTOMER, request.getAttribute("memberRole"));
        verify(filterChain, times(1)).doFilter(request, response);

    }

    @Test
    void 만료된_토큰_리프레시토큰_검사() throws ServletException, IOException {
        //given
        request.setRequestURI("/endpoint");
        request.addHeader("Authorization", "Bearer expired_access_token");

        Claims claims = mock(Claims.class);
        when(jwtUtil.extractClaims("expired_access_token")).thenReturn(claims); // 토큰 파싱
        when(claims.getSubject()).thenReturn("1"); //id
        when(claims.get("email", String.class)).thenReturn("test@naver.com");
        when(claims.get("memberRole", String.class)).thenReturn(MemberRole.CUSTOMER.toString());
        when(claims.getExpiration()).thenReturn(new Date(System.currentTimeMillis()-10000)); //만료 시간 설정 (이미 만료됨)

        RefreshToken refreshToken = mock(RefreshToken.class);
        when(tokenRepository.findByEmail("test@naver.com")).thenReturn(Optional.of(refreshToken)); //해당 이메일로 리프레시 토큰 찾기
        when(refreshToken.getRefreshToken()).thenReturn("refresh_token");
        when(jwtUtil.refreshAccessToken("refresh_token")).thenReturn("newAccessToken"); //리프레시 토큰으로 새로운 액세스 토큰 발급

        response.setHeader("Authorization", "Bearer newAccessToken");
        // when
        jwtFilter.doFilter(request, response, filterChain);

        // then
        assertEquals("Bearer newAccessToken", response.getHeader("Authorization"));
        verify(filterChain, times(1)).doFilter(request, response);

    }


}
