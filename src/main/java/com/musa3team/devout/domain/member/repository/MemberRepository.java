package com.musa3team.devout.domain.member.repository;

import com.musa3team.devout.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
