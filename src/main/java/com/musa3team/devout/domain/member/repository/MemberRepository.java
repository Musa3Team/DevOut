package com.musa3team.devout.domain.member.repository;

import com.musa3team.devout.domain.member.entity.Member;
import com.musa3team.devout.domain.member.entity.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmailAndMemberRole(String email, MemberRole memberRole);

    Optional<Member> findByEmailAndMemberRole(String email, MemberRole memberRole);


}
