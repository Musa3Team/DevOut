package com.musa3team.devout.domain.member.controller;

import com.musa3team.devout.common.jwt.JwtUtil;
import com.musa3team.devout.domain.member.dto.request.DeleteMemberRequest;
import com.musa3team.devout.domain.member.dto.request.ModifyInfoRequest;
import com.musa3team.devout.domain.member.dto.request.ModifyPasswordRequest;
import com.musa3team.devout.domain.member.dto.response.MemberResponse;
import com.musa3team.devout.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Member 관리 API", description = "사용자의 회원 정보 수정, 비밀번호 수정, 회원 탈퇴 기능 API 입니다.")
public class MemberController {
    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @PatchMapping("/{id}")
    @Operation(summary = "회원 정보 수정", description = "로그인 된 사용자가 본인의 이름, 주소, 전화번호만 수정 가능합니다.")
    public ResponseEntity<MemberResponse> modify(
            @PathVariable Long id,
            @RequestBody ModifyInfoRequest modifyInfoRequest,
            HttpServletRequest request) {

        String token = jwtUtil.extractToken(request);

        log.info("*** token: {}", token);

        memberService.isEqualsId(id, token); // 토큰에서의 id와 일치하는지 확인하는 로직

        MemberResponse memberResponse = memberService.modifyInfo(id, modifyInfoRequest);
        return new ResponseEntity<>(memberResponse, HttpStatus.OK);


    }

    @PatchMapping("/{id}/password")
    @Operation(summary = "비밀번호 변경", description = "로그인 된 사용자가 본인의 비밀번호만 변경 가능합니다.")
    public ResponseEntity<String> modifyPassword(
            @PathVariable Long id,
            @RequestBody ModifyPasswordRequest passwordRequest,
            HttpServletRequest request) {

        String token = jwtUtil.extractToken(request);

        memberService.isEqualsId(id, token); // 토큰에서의 id와 일치하는지 확인하는 로직

        memberService.modifyPassword(id, passwordRequest);

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    @Operation(summary = "회원 탈퇴", description = "soft-delete가 적용되었습니다.")
    public ResponseEntity<String> deleteMember(
            @PathVariable Long id,
            @RequestBody DeleteMemberRequest deleteMemberRequest,
            HttpServletRequest request) {

        String token = jwtUtil.extractToken(request);

        memberService.isEqualsId(id, token);

        memberService.deleteMember(id, deleteMemberRequest);

        return new ResponseEntity<>(HttpStatus.OK);

    }

}
