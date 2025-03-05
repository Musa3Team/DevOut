package com.musa3team.devout.common.jwt;

import com.musa3team.devout.domain.member.entity.Member;
import com.musa3team.devout.domain.member.entity.MemberRole;
import com.musa3team.devout.domain.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
public class JwtUtil {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final long ACCESS_TOKEN_EXPIRATION = 30 * 60 * 1000L; // 30분
    private static final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000L; // 일주일
    private final MemberRepository memberRepository;

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;

    public JwtUtil(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // access 토큰 생성
    public String createAccessToken(Long userId, String email, MemberRole memberRole) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setHeaderParam("typ", "JWT")
                        .setSubject(String.valueOf(userId)) //사용자 식별 값 설정
                        .claim("email", email)
                        .claim("memberRole", memberRole)
                        .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_EXPIRATION)) // 만료시간 지정
                        .setIssuedAt(date)
                        .signWith(key, SignatureAlgorithm.HS256) // 암호화 알고리즘
                        .compact();
    }

    // refresh 토큰 생성
    public String createRefreshToken(Long userId, String email, MemberRole memberRole) {
        Date date = new Date();
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

    }

    public void setHeader(HttpServletResponse response, String accessToken){
        response.addHeader("Authorization", accessToken);
    }

    // Bearer 제외한 토큰 뒷부분 추출
    public String substringToken(String token) {
        if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            return token.substring(7);
        }
        throw new IllegalArgumentException("Not Found Token");
    }

    // 토큰으로 유저 정보 가져오기
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Long extractMemberId(String token){
        Claims claims = extractClaims(token);
        return Long.parseLong(claims.getSubject());
    }

    public String refreshAccessToken(String refreshToken) {
        Claims claims = extractClaims(refreshToken);

        if(claims.getExpiration().before(new Date())) {
            throw new IllegalArgumentException("Expired or invalid JWT token");
        }

        String email = claims.get("email", String.class);
        String memberRole = claims.get("memberRole", String.class);
        log.info("****** refresh access token email:{}", email);
        log.info("****** refresh access token memberRole:{}", memberRole);


        Member member = memberRepository.findByEmailAndMemberRole(email, MemberRole.valueOf(memberRole))
                .orElseThrow(() -> new RuntimeException("해당멤버가 존재하지 않습니다."));

        return createAccessToken(member.getId(), email, MemberRole.valueOf(memberRole));
    }

    public boolean isTokenExpired(String token) {
        Claims claims = extractClaims(token);
        return claims.getExpiration().before(new Date());
    }


}