package com.musa3team.devout.domain.member;

import com.musa3team.devout.common.config.PasswordEncoder;
import com.musa3team.devout.common.jwt.JwtUtil;
import com.musa3team.devout.domain.member.dto.request.ModifyInfoRequest;
import com.musa3team.devout.domain.member.dto.response.MemberResponse;
import com.musa3team.devout.domain.member.entity.Member;
import com.musa3team.devout.domain.member.entity.MemberRole;
import com.musa3team.devout.domain.member.repository.MemberRepository;
import com.musa3team.devout.domain.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void 회원정보_수정_성공(){

        //given
        Long memberId = 1L;
        Member existingMember = new Member(
                "이름테스트",
                "test@naver.com",
                "Qlalfqjsgh33!",
                "서울시어쩌구",
                "010-1234-1234",
                MemberRole.CUSTOMER.name()
        );

        ReflectionTestUtils.setField(existingMember, "id", memberId);

        ModifyInfoRequest modifyInfoRequest = new ModifyInfoRequest(
                "새 이름",
                "새 주소",
                "010-2222-2222"
        );


        given(memberRepository.findByIdOrElseThrow(memberId)).willReturn(existingMember);

        //when
        MemberResponse memberResponse = memberService.modifyInfo(memberId, modifyInfoRequest);

        //then
        assertNotNull(memberResponse);
        assertEquals("새 이름", memberResponse.getName());
        assertEquals("새 주소", memberResponse.getAddress());
        assertEquals("010-2222-2222", memberResponse.getPhoneNumber());

    }
}
