package com.musa3team.devout.store;

import com.musa3team.devout.common.constants.StoreCategory;
import com.musa3team.devout.domain.store.entity.Store;
import com.musa3team.devout.domain.store.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class StoreRepositoryTest {

    @Autowired
    private StoreRepository storeRepository;

    @Test
     void 가게를_다건_조회할_수_있다() {
        // given
        String name = "치치킨";
        StoreCategory category = StoreCategory.CHICKEN;

        Store store = new Store();
        store.setName(name);
        store.setCategory(category);

        String name2 = "치킨2";
        Store store2 = new Store();
        store2.setName(name2);
        store2.setCategory(category);

        storeRepository.save(store);
        storeRepository.save(store2);
        //when
        List<Store> byId = storeRepository.findAllByNameAndCategory(store.getName(), store.getCategory());

        //then
        assertEquals("치치킨", byId.get(0).getName());
        assertEquals(StoreCategory.CHICKEN, byId.get(0).getCategory());
        assertEquals("치킨2", byId.get(1).getName());
        assertEquals(StoreCategory.CHICKEN, byId.get(1).getCategory());
    }
}
