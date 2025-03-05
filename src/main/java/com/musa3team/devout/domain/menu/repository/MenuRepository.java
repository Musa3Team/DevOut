package com.musa3team.devout.domain.menu.repository;

import com.musa3team.devout.domain.menu.entity.Menu;
import com.musa3team.devout.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    Optional<Menu> findByIdAndStore(Long id, Store store);
}
