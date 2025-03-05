package com.musa3team.devout.domain.store.repository;

import com.musa3team.devout.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findById(Long storeId);
}
