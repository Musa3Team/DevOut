package com.musa3team.devout.domain.menu.repository;

import com.musa3team.devout.domain.menu.entity.Menu;
import com.musa3team.devout.domain.menu.entity.MenuCategory;
import com.musa3team.devout.domain.store.entity.Store;
import com.musa3team.devout.domain.store.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MenuRepositoryTest {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Test
    void 가게와_ID로_메뉴를_조회할_수_있다() {

        // given

        String name = "탕수육";
        int price = 12000;
        String contents = "맛있는 탕수육";
        MenuCategory category = MenuCategory.MAIN_DISHES;

        Store store = new Store();
        storeRepository.save(store);

        Menu menu = new Menu(store, name, price, contents, category);
        menuRepository.save(menu);

        // when

        Menu foundMenu = menuRepository.findByIdAndStore(menu.getId(), store).orElse(null);

        // then

        assertNotNull(foundMenu);
        assertEquals(name, foundMenu.getName());
        assertEquals(price, foundMenu.getPrice());
        assertEquals(contents, foundMenu.getContents());
        assertEquals(category, foundMenu.getCategory());
        assertEquals(store, foundMenu.getStore());

    }

    @Test
    void 존재하지_않는_메뉴는_조회되지_않는다() {

        // given

        Store store = new Store();
        storeRepository.save(store);

        // when

        Menu foundMenu = menuRepository.findByIdAndStore(9999L, store).orElse(null);

        // then

        assertNull(foundMenu);
    }

}