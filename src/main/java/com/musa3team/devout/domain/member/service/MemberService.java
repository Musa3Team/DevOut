package com.musa3team.devout.domain.member.service;

import com.musa3team.devout.domain.member.dto.request.ModifyInfoRequest;
import com.musa3team.devout.domain.member.dto.response.MemberResponse;
import com.musa3team.devout.domain.member.entity.Member;
import com.musa3team.devout.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public MemberResponse modifyInfo(Long id, ModifyInfoRequest updateInfoRequest) {
        Member member = memberRepository.findByIdOrElseThrow(id);

        if(updateInfoRequest.getName() != null) {
            member.modifyName(updateInfoRequest.getName());
        }
        if(updateInfoRequest.getAddress() != null) {
            member.modifyAddress(updateInfoRequest.getAddress());
        }
        if(updateInfoRequest.getPhoneNumber() != null) {
            member.modifyPhoneNumber(updateInfoRequest.getPhoneNumber());
        }

        return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getAddress(), member.getPhoneNumber());
    }
}
