package com.musa3team.devout.domain.member.controller;

import com.musa3team.devout.common.jwt.JwtUtil;
import com.musa3team.devout.domain.member.dto.request.DeleteMemberRequest;
import com.musa3team.devout.domain.member.dto.request.ModifyInfoRequest;
import com.musa3team.devout.domain.member.dto.request.ModifyPasswordRequest;
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
            @RequestBody ModifyInfoRequest modifyInfoRequest,
            HttpServletRequest request) {

        String authorizationHeader = request.getHeader("Authorization");

        //헤더가 비어있거나 Bearer 로 시작하지 않는 경우 에러
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.info("Authorization 헤더가 없거나 잘못된 형식입니다.");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // Bearer를 제외하고 실제 토큰만 추출하는 로직
        String token = jwtUtil.substringToken(authorizationHeader);
        log.info("*** token: {}", token);

        memberService.isEqualsId(id, token); // 토큰에서의 id와 일치하는지 확인하는 로직

        MemberResponse memberResponse = memberService.modifyInfo(id, modifyInfoRequest);
        return new ResponseEntity<>(memberResponse, HttpStatus.OK);


    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<String> modifyPassword(
            @PathVariable Long id,
            @RequestBody ModifyPasswordRequest passwordRequest,
            HttpServletRequest request) {

        String authorizationHeader = request.getHeader("Authorization");

        //헤더가 비어있거나 Bearer 로 시작하지 않는 경우 에러
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.info("Authorization 헤더가 없거나 잘못된 형식입니다.");
            return new ResponseEntity<>("헤더가 없거나 잘못된 형식", HttpStatus.UNAUTHORIZED);
        }

        // Bearer를 제외하고 실제 토큰만 추출하는 로직
        String token = jwtUtil.substringToken(authorizationHeader);
        log.info("*** token: {}", token);

        memberService.isEqualsId(id, token); // 토큰에서의 id와 일치하는지 확인하는 로직

        memberService.modifyPassword(id, passwordRequest);

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMember(
            @PathVariable Long id,
            @RequestBody DeleteMemberRequest deleteMemberRequest,
            HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        //헤더가 비어있거나 Bearer 로 시작하지 않는 경우 에러
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.info("Authorization 헤더가 없거나 잘못된 형식입니다.");
            return new ResponseEntity<>("헤더가 없거나 잘못된 형식", HttpStatus.UNAUTHORIZED);
        }

        // Bearer를 제외하고 실제 토큰만 추출하는 로직
        String token = jwtUtil.substringToken(authorizationHeader);
        log.info("*** token: {}", token);

        memberService.isEqualsId(id, token);

        memberService.deleteMember(id, deleteMemberRequest);

        return new ResponseEntity<>(HttpStatus.OK);

    }

}
