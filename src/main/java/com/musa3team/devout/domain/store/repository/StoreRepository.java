package com.musa3team.devout.domain.store.repository;

import com.musa3team.devout.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByIdAndOpenTimeBeforeAndCloseTimeAfterAndStatus(Long id, LocalTime before, LocalTime after, String status);
}
