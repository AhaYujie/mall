package online.ahayujie.mall.admin.ums.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.ums.bean.dto.CreateMenuParam;
import online.ahayujie.mall.admin.ums.bean.dto.UpdateMenuParam;
import online.ahayujie.mall.admin.ums.bean.model.Menu;
import online.ahayujie.mall.admin.ums.exception.IllegalMenuException;
import online.ahayujie.mall.admin.ums.exception.IllegalMenuVisibilityException;
import online.ahayujie.mall.admin.ums.exception.IllegalParentMenuException;
import online.ahayujie.mall.admin.ums.mapper.MenuMapper;
import online.ahayujie.mall.admin.ums.service.MenuService;
import online.ahayujie.mall.common.api.CommonPage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class MenuServiceImplTest {
    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private MenuService menuService;

    @Test
    void createMenu() {
        CreateMenuParam param;

        // illegal parentId
        param = new CreateMenuParam();
        param.setParentId(null);
        CreateMenuParam finalParam = param;
        Throwable throwable1 = assertThrows(IllegalParentMenuException.class, () -> menuService.createMenu(finalParam));
        log.debug(throwable1.getMessage());

        // illegal hidden
        param = new CreateMenuParam();
        param.setParentId(Menu.NON_PARENT_ID);
        param.setHidden(-1);
        CreateMenuParam finalParam1 = param;
        Throwable throwable2 = assertThrows(IllegalMenuVisibilityException.class, () -> menuService.createMenu(finalParam1));
        log.debug(throwable2.getMessage());

        // legal
        param = new CreateMenuParam();
        param.setName("test menu");
        param.setParentId(Menu.NON_PARENT_ID);
        param.setTitle("test title");
        param.setSort(1);
        param.setHidden(Menu.VISIBILITY.SHOW.getValue());
        List<Menu> oldMenus = menuService.list();
        menuService.createMenu(param);
        List<Menu> newMenus = menuService.list();
        assertEquals(oldMenus.size() + 1, newMenus.size());
        log.debug("oldMenus: " + oldMenus);
        log.debug("newMenus: " + newMenus);
    }

    @Test
    void updateMenu() {
        Long id;
        UpdateMenuParam param;

        // illegal menu
        id = null;
        param = new UpdateMenuParam();
        Long finalId2 = id;
        UpdateMenuParam finalParam2 = param;
        Throwable throwable3 = assertThrows(IllegalMenuException.class, () -> menuService.updateMenu(finalId2, finalParam2));
        log.debug(throwable3.getMessage());

        Menu menu = new Menu();
        menu.setTitle("for test");
        menuMapper.insert(menu);

        // illegal parentId
        id = menu.getId();
        param = new UpdateMenuParam();
        param.setParentId(-1L);
        UpdateMenuParam finalParam = param;
        Long finalId = id;
        Throwable throwable1 = assertThrows(IllegalParentMenuException.class, () -> menuService.updateMenu(finalId, finalParam));
        log.debug(throwable1.getMessage());

        // illegal hidden
        id = menu.getId();
        param = new UpdateMenuParam();
        param.setParentId(Menu.NON_PARENT_ID);
        param.setHidden(-1);
        Long finalId1 = id;
        UpdateMenuParam finalParam1 = param;
        Throwable throwable2 = assertThrows(IllegalMenuVisibilityException.class, () -> menuService.updateMenu(finalId1, finalParam1));
        log.debug(throwable2.getMessage());

        // legal
        id = menu.getId();
        param = new UpdateMenuParam();
        param.setName("new name");
        param.setTitle("new title");
        param.setIcon("new icon");
        param.setSort(1);
        param.setHidden(Menu.VISIBILITY.HIDDEN.getValue());
        param.setParentId(Menu.NON_PARENT_ID);
        Menu oldMenu = menuService.getById(id);
        menuService.updateMenu(id, param);
        Menu newMenu = menuService.getById(id);
        assertNotEquals(oldMenu, newMenu);
        log.debug("oldMenu: " + oldMenu);
        log.debug("newMenu: " + newMenu);
    }

    @Test
    void queryByParentId() {
        Long parentId;
        int pageNum, pageSize;

        // not exist parentId
        parentId = null;
        pageNum = 1;
        pageSize = 5;
        CommonPage<Menu> result1 = menuService.queryByParentId(parentId, pageSize, pageNum);
        assertEquals(0, result1.getTotal());
        log.debug("result1: " + result1);

        // exist parentId
        Menu parent = new Menu();
        parent.setParentId(Menu.NON_PARENT_ID);
        menuMapper.insert(parent);
        List<Menu> menus = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Menu menu = new Menu();
            menu.setParentId(parent.getId());
            menu.setTitle("for test: " + i);
            menus.add(menu);
        }
        menus.forEach(menuMapper::insert);
        parentId = parent.getId();
        CommonPage<Menu> result2 = menuService.queryByParentId(parentId, 5, 1);
        log.debug("result2: " + result2);
        assertEquals(5, result2.getData().size());
        assertEquals(menus.size(), result2.getTotal());
    }

    @Test
    void removeById() {
        // exist
        Menu menu = new Menu();
        menu.setName("test menu");
        menu.setParentId(Menu.NON_PARENT_ID);
        menu.setHidden(Menu.VISIBILITY.SHOW.getValue());
        menuMapper.insert(menu);
        List<Menu> oldMenus = menuService.list();
        menuService.removeById(menu.getId());
        List<Menu> newMenus = menuService.list();
        log.debug("oldMenus: " + oldMenus);
        log.debug("newMenus: " + newMenus);
        assertEquals(oldMenus.size() - 1, newMenus.size());

        // null
        Long id = null;
        oldMenus = menuService.list();
        menuService.removeById(id);
        newMenus = menuService.list();
        log.debug("oldMenus: " + oldMenus);
        log.debug("newMenus: " + newMenus);
        assertEquals(oldMenus.size(), newMenus.size());


        // not exist
        id = -1L;
        oldMenus = menuService.list();
        menuService.removeById(id);
        newMenus = menuService.list();
        log.debug("oldMenus: " + oldMenus);
        log.debug("newMenus: " + newMenus);
        assertEquals(oldMenus.size(), newMenus.size());

        // with sub menu
        Menu parent = new Menu();
        parent.setParentId(Menu.NON_PARENT_ID);
        parent.setName("parent menu");
        menuMapper.insert(parent);
        Menu subMenu = new Menu();
        subMenu.setParentId(parent.getId());
        subMenu.setName("sub menu");
        menuMapper.insert(subMenu);
        List<Menu> oldMenus1 = menuService.list();
        menuService.removeById(parent.getId());
        List<Menu> newMenus1 = menuService.list();
        assertEquals(oldMenus1.size() - 2, newMenus1.size());
    }
}