package com.musa3team.devout.store;

import com.musa3team.devout.common.constants.StoreCategory;
import com.musa3team.devout.domain.store.entity.Store;
import com.musa3team.devout.domain.store.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class StoreRepositoryTest {

    @Autowired
    private StoreRepository storeRepository;

    @Test
     void 가게를_조회할_수_있다() {
        // given
        String name = "치치킨";
        StoreCategory category = StoreCategory.CHICKEN;

        Store store = new Store();
        store.setName(name);
        store.setCategory(category);

        storeRepository.save(store);
        //when
        Optional<Store> byId = storeRepository.findById(store.getId());

        //then
        assertTrue(byId.isPresent());
        assertEquals("치치킨", byId.get().getName());
        assertEquals(StoreCategory.CHICKEN, byId.get().getCategory());
    }
}
