package online.ahayujie.mall.admin.ums.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.ums.bean.dto.CreateMenuParam;
import online.ahayujie.mall.admin.ums.bean.dto.UpdateMenuParam;
import online.ahayujie.mall.admin.ums.bean.model.Menu;
import online.ahayujie.mall.admin.ums.exception.admin.IllegalMenuException;
import online.ahayujie.mall.admin.ums.exception.admin.IllegalMenuVisibilityException;
import online.ahayujie.mall.admin.ums.exception.admin.IllegalParentMenuException;
import online.ahayujie.mall.admin.ums.service.MenuService;
import online.ahayujie.mall.common.api.CommonPage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class MenuServiceImplTest {
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

        // illegal parentId
        id = 1L;
        param = new UpdateMenuParam();
        param.setParentId(-1L);
        UpdateMenuParam finalParam = param;
        Long finalId = id;
        Throwable throwable1 = assertThrows(IllegalParentMenuException.class, () -> menuService.updateMenu(finalId, finalParam));
        log.debug(throwable1.getMessage());

        // illegal hidden
        id = 1L;
        param = new UpdateMenuParam();
        param.setParentId(Menu.NON_PARENT_ID);
        param.setHidden(-1);
        Long finalId1 = id;
        UpdateMenuParam finalParam1 = param;
        Throwable throwable2 = assertThrows(IllegalMenuVisibilityException.class, () -> menuService.updateMenu(finalId1, finalParam1));
        log.debug(throwable2.getMessage());

        // legal
        id = 1L;
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
        Integer pageNum, pageSize;

        // not exist parentId
        parentId = null;
        pageNum = 1;
        pageSize = 5;
        CommonPage<Menu> result1 = menuService.queryByParentId(parentId, pageSize, pageNum);
        assertEquals(0, result1.getTotal());
        log.debug("result1: " + result1);

        // exist parentId
        parentId = Menu.NON_PARENT_ID;
        pageNum = 1;
        pageSize = 2;
        CommonPage<Menu> result2 = menuService.queryByParentId(parentId, pageSize, pageNum);
        assertNotEquals(0, result2.getTotal());
        log.debug("result2: " + result2);
    }
}