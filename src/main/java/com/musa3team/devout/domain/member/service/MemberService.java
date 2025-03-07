package com.musa3team.devout.domain.member.service;

import com.musa3team.devout.common.config.PasswordEncoder;
import com.musa3team.devout.common.jwt.JwtUtil;
import com.musa3team.devout.domain.member.dto.request.DeleteMemberRequest;
import com.musa3team.devout.domain.member.dto.request.ModifyInfoRequest;
import com.musa3team.devout.domain.member.dto.request.ModifyPasswordRequest;
import com.musa3team.devout.domain.member.dto.response.MemberResponse;
import com.musa3team.devout.domain.member.entity.Member;
import com.musa3team.devout.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberResponse modifyInfo(Long id, ModifyInfoRequest modifyInfoRequest) {
        Member member = memberRepository.findByIdOrElseThrow(id);

        if(modifyInfoRequest.getName() != null) {
            member.modifyName(modifyInfoRequest.getName());
        }
        if(modifyInfoRequest.getAddress() != null) {
            member.modifyAddress(modifyInfoRequest.getAddress());
        }
        if(modifyInfoRequest.getPhoneNumber() != null) {
            member.modifyPhoneNumber(modifyInfoRequest.getPhoneNumber());
        }

        return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getAddress(), member.getPhoneNumber());
    }

    @Transactional
    public void modifyPassword(Long id, ModifyPasswordRequest passwordRequest) {
        Member member = memberRepository.findByIdOrElseThrow(id);
        validPassword(passwordRequest.getOldPassword(), member.getPassword()); // 입력한 비밀번호가 db에 저장되어있는 비밀번호랑 일치하는지 확인하는 로직

        // 새로운 비밀번호가 null이 아니고, 기존비밀번호와 일치하지 않을 때만 수정
        if(passwordRequest.getNewPassword() != null && !passwordRequest.getNewPassword().equals(passwordRequest.getOldPassword())) {
            member.modifyPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));
        }
    }

    public void isEqualsId(Long id, String token){
        Long idByToken = jwtUtil.extractMemberId(token);
        if(!idByToken.equals(id)) {
            log.info("권한 없음 : 요청id와 토큰 id 불일치");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }
    }

    private void validPassword(String inputPassword, String existPassword) {
        if(!passwordEncoder.matches(inputPassword, existPassword)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "비밀번호가 일치하지 않습니다.");
        }
    }

    @Transactional
    public void deleteMember(Long id, DeleteMemberRequest deleteMemberRequest) {
        Member member = memberRepository.findByIdOrElseThrow(id);

        validPassword(deleteMemberRequest.getPassword(), member.getPassword());

        member.deleteMember(LocalDateTime.now());

    }
}
