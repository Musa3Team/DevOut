package com.musa3team.devout.domain.member.repository;

import com.musa3team.devout.domain.member.entity.Member;
import com.musa3team.devout.domain.member.entity.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmailAndMemberRole(String email, MemberRole memberRole);

    Optional<Member> findByEmailAndMemberRole(String email, MemberRole memberRole);


    default Member findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 아이디는 존재하지 않습니다."));
    }
}
