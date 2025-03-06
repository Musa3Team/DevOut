package com.musa3team.devout.common.jwt;

import com.musa3team.devout.domain.member.entity.MemberRole;
import com.musa3team.devout.domain.member.entity.RefreshToken;
import com.musa3team.devout.domain.member.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import jakarta.servlet.*;


import java.io.IOException;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter implements Filter {
    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;
    private static final String[] WHITELIST = {"/auth/signup", "/auth/login", "/swagger-ui/*", "/v3/api-docs/**"};


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        String url = httpRequest.getRequestURI();

        if(isWhiteList(url)){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String accessToken = httpRequest.getHeader("Authorization");
        if(accessToken != null && accessToken.startsWith("Bearer ")){
            try {
                String token = accessToken.substring(7);
                Claims claims = jwtUtil.extractClaims(token);

                String email = claims.get("email", String.class);
                Long memberId = Long.parseLong(claims.getSubject());
                MemberRole memberRole = MemberRole.valueOf(claims.get("memberRole", String.class));

                if(claims.getExpiration().before(new Date())){
                    RefreshToken refreshToken = tokenRepository.findByEmail(email)
                            .orElseThrow(() -> new RuntimeException("리프레시 토큰이 존재하지 않습니다."));

                    String newAccessToken = jwtUtil.refreshAccessToken(refreshToken.getRefreshToken());
                    httpResponse.setHeader("Authorization", "Bearer " + newAccessToken);
                }


                log.info("******* 토큰에서 추출한 id = {}, email = {}", memberId, email);

                httpRequest.setAttribute("memberId", memberId);
                httpRequest.setAttribute("email", email);
                httpRequest.setAttribute("memberRole", memberRole);

            } catch (SecurityException | MalformedJwtException e) {
                log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.", e);
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않는 JWT 서명입니다.");
            } catch (ExpiredJwtException e) {
                log.error("Expired JWT token, 만료된 JWT token 입니다.", e);
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "만료된 JWT 토큰입니다.");
            } catch (UnsupportedJwtException e) {
                log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.", e);
                httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "지원되지 않는 JWT 토큰입니다.");
            } catch (Exception e) {
                log.error("Invalid JWT token, 유효하지 않는 JWT 토큰 입니다.", e);
                httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "유효하지 않는 JWT 토큰입니다.");
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);


    }

    @Override
    public void destroy(){
        Filter.super.destroy();
    }

    private boolean isWhiteList(String requestURI) {
        return PatternMatchUtils.simpleMatch(WHITELIST,requestURI);
    }

}