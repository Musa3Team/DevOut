package com.musa3team.devout.domain.menu.repository;

import com.musa3team.devout.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findAllByStoreId(Long id);

    Optional<Menu> findByIdAndStoreId(Long menuId, Long storeId);
}
