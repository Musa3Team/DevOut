package com.musa3team.devout.common.jwt;

import com.musa3team.devout.domain.member.entity.MemberRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final long TOKEN_EXPIRATION = 30 * 60 * 1000L; // 30분

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createToken(Long userId, String email, MemberRole memberRole) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setHeaderParam("typ", "JWT")
                        .setSubject(String.valueOf(userId)) //사용자 식별 값 설정
                        .claim("email", email)
                        .claim("userRole", memberRole)
                        .setExpiration(new Date(date.getTime() + TOKEN_EXPIRATION)) // 만료시간 지정
                        .setIssuedAt(date)
                        .signWith(key, SignatureAlgorithm.HS256) // 암호화 알고리즘
                        .compact();
    }

    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        throw new IllegalArgumentException("Not Found Token");
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}