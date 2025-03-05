package com.musa3team.devout.domain.member.repository;

import com.musa3team.devout.domain.member.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<RefreshToken, Long> {
    boolean existsByRefreshToken(String token);

    Optional<RefreshToken> findByEmail(String email);
}
