package com.musa3team.devout.domain.store.repository;

import com.musa3team.devout.common.constants.StoreCategory;
import com.musa3team.devout.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    //카테고리가 같은지 찾음
    //이름이 비어있지 않으며 이름이 like name 조건에 해당하면 우선순위 0 아니면 1로 이름 오름차순정렬
    @Query("SELECT s FROM Store s WHERE s.category = :category " +
            "ORDER BY CASE WHEN :name IS NOT NULL AND s.name LIKE %:name% THEN 0 ELSE 1 END, s.name ASC")
    List<Store> findAllByNameAndCategory(@Param("name") String name, @Param("category") StoreCategory category);
}
