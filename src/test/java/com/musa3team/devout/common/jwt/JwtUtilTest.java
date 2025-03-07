package com.musa3team.devout.common.jwt;

import com.musa3team.devout.domain.member.entity.MemberRole;
import com.musa3team.devout.domain.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
public class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    private final String secretKey = "mySecretKeyForJwtTestingMySecretKeyForJwtTesting";

    @Mock
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

        ReflectionTestUtils.setField(jwtUtil, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtUtil, "key", key);

    }

    @Test
    void 액세스토큰_정상_생성(){
        //given
        Long memberId = 1L;
        String email = "test@naver.com";
        MemberRole memberRole = MemberRole.CUSTOMER;

        //when
        String accessToken = jwtUtil.createAccessToken(memberId, email, memberRole);

        //then
        assertNotNull(accessToken);
        assertTrue(accessToken.startsWith("Bearer "));
    }

    @Test
    void 리프레시토큰_정상_생성(){
        //given
        Long memberId = 1L;
        String email = "test@naver.com";
        MemberRole memberRole = MemberRole.CUSTOMER;

        //when
        String refreshToken = jwtUtil.createRefreshToken(memberId, email, memberRole);

        //then
        assertNotNull(refreshToken);
    }

    @Test
    void 토큰에서_멤버ID_추출_성공(){
        //given
        Long memberId = 1L;
        String email = "test@naver.com";
        MemberRole memberRole = MemberRole.CUSTOMER;
        String accessToken = jwtUtil.createAccessToken(memberId, email, memberRole);

        //when
        Long extractMemberId = jwtUtil.extractMemberId(accessToken.substring(7));

        //then
        assertNotNull(extractMemberId);
        assertEquals(memberId, extractMemberId);
    }

    @Test
    void 토큰에서_멤버Role_추출_성공(){
        //given
        Long memberId = 1L;
        String email = "test@naver.com";
        MemberRole memberRole = MemberRole.CUSTOMER;
        String accessToken = jwtUtil.createAccessToken(memberId, email, memberRole);

        //when
        MemberRole extractMemberRole = jwtUtil.extractMemberRole(accessToken.substring(7));

        //then
        assertNotNull(extractMemberRole);
        assertEquals(memberRole, extractMemberRole);


    }

//    @Test
//    void 토큰_만료_여부_테스트(){
//
//        String expiredToken = Jwts.builder()
//                .setSubject("testUser")  // 사용자 정보 설정 (필요 없음, 그냥 넣는 거)
//                .setExpiration(new Date(System.currentTimeMillis() - 1000)) // 과거 시간 → 이미 만료된 토큰
//                .signWith(SignatureAlgorithm.HS256) // 서명 키 설정 (임시 키)
//                .compact();
//
//        // when
//        boolean isExpired = jwtUtil.isTokenExpired(expiredToken);
//
//        // then
//        assertTrue(isExpired);
//
//
//    }

    @Test
    void 토큰_유효성_검사_성공(){
        //given
        Long memberId = 1L;
        String email = "test@naver.com";
        MemberRole memberRole = MemberRole.CUSTOMER;
        String accessToken = jwtUtil.createAccessToken(memberId, email, memberRole);

        // when
        Claims claims = jwtUtil.extractClaims(accessToken.substring(7));

        // then
        assertNotNull(claims);
        assertEquals(memberId.toString(), claims.getSubject());
        assertEquals(email, claims.get("email", String.class));
        assertEquals(memberRole.name(), claims.get("memberRole", String.class));

    }



}
