package com.musa3team.devout.domain.member.repository;

import com.musa3team.devout.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByIdAndMemberRole(Long id, String userRole);
}
