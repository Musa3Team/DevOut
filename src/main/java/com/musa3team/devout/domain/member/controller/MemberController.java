package com.musa3team.devout.domain.member.controller;

import com.musa3team.devout.common.jwt.JwtUtil;
import com.musa3team.devout.domain.member.dto.request.ModifyInfoRequest;
import com.musa3team.devout.domain.member.dto.response.MemberResponse;
import com.musa3team.devout.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @PatchMapping("/{id}")
    public ResponseEntity<MemberResponse> modify(
            @PathVariable Long id,
            @RequestBody ModifyInfoRequest updateInfoRequest,
            HttpServletRequest request) {

        log.info("=== PATCH /member/{id} 실행됨 ===");

        String authorizationHeader = request.getHeader("Authorization");

        //헤더가 비어있거나 Bearer 로 시작하지 않는 경우 에러
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.info("Authorization 헤더가 없거나 잘못된 형식입니다.");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // Bearer를 제외하고 실제 토큰만 추출하는 로직
        String token = jwtUtil.substringToken(authorizationHeader);
        log.info("*** token: {}", token);

        // 토큰에서의 id와 일치하는지 확인하는 로직
        Long tokenMemberId = jwtUtil.extractMemberId(token); //토큰에서 memberId 추출
        log.info("*** 현재 로그인된 사용자 id = {}", tokenMemberId);

        if(!tokenMemberId.equals(id)) {
            log.info("권한 없음 : 요청id와 토큰 id 불일치");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        MemberResponse memberResponse = memberService.modifyInfo(id, updateInfoRequest);
        return new ResponseEntity<>(memberResponse, HttpStatus.OK);


    }

}
