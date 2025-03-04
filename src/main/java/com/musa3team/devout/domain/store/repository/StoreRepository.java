package com.musa3team.devout.domain.store.repository;

import com.musa3team.devout.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
