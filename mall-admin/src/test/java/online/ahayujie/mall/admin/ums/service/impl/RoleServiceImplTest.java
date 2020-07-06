package online.ahayujie.mall.admin.ums.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.ums.bean.dto.CreateRoleParam;
import online.ahayujie.mall.admin.ums.bean.dto.UpdateRoleParam;
import online.ahayujie.mall.admin.ums.bean.model.Admin;
import online.ahayujie.mall.admin.ums.bean.model.Menu;
import online.ahayujie.mall.admin.ums.bean.model.Resource;
import online.ahayujie.mall.admin.ums.bean.model.Role;
import online.ahayujie.mall.admin.ums.event.DeleteAdminEvent;
import online.ahayujie.mall.admin.ums.exception.IllegalMenuException;
import online.ahayujie.mall.admin.ums.exception.IllegalResourceException;
import online.ahayujie.mall.admin.ums.exception.IllegalRoleException;
import online.ahayujie.mall.admin.ums.service.AdminService;
import online.ahayujie.mall.admin.ums.service.MenuService;
import online.ahayujie.mall.admin.ums.service.ResourceService;
import online.ahayujie.mall.admin.ums.service.RoleService;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.common.bean.model.Base;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class RoleServiceImplTest {
    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Test
    void getRoleListByAdminId() {
        Long adminId = null;
        // 用 null 查询不存在的用户
        List<Role> result1 = roleService.getRoleListByAdminId(adminId);
        assertEquals(0, result1.size());

        // 查询存在的用户但没有角色
        adminId = 2L;
        List<Role> result2 = roleService.getRoleListByAdminId(adminId);
        assertEquals(0, result2.size());

        // 查询存在的用户且拥有角色
        adminId = 1L;
        List<Role> result3 = roleService.getRoleListByAdminId(adminId);
        assertNotEquals(0, result3.size());
        log.debug(result3.toString());
    }

    @Test
    void createRole() {
        List<Role> oldRoles = roleService.list();

        CreateRoleParam role = new CreateRoleParam();
        role.setName("test");
        role.setDescription("test Description");
        role.setStatus(Role.STATUS.ACTIVE.getValue());
        roleService.createRole(role);

        List<Role> newRoles = roleService.list();
        assertEquals(oldRoles.size() + 1, newRoles.size());
        log.debug("oldRoles: " + oldRoles);
        log.debug("newRoles: " + newRoles);
    }

    @Test
    void updateRole() {
        UpdateRoleParam newRole = new UpdateRoleParam();

        // illegal role
        UpdateRoleParam finalNewRole = newRole;
        Throwable throwable = assertThrows(IllegalRoleException.class, () -> roleService.updateRole(null, finalNewRole));
        log.debug(throwable.getMessage());

        // legal role
        Long id = 1L;
        Role oldRole = roleService.getById(id);
        newRole.setDescription("new role");
        roleService.updateRole(id, newRole);
        Role updatedRole = roleService.getById(id);
        log.debug("oldRole: " + oldRole);
        log.debug("updatedRole: " + updatedRole);
        assertNotEquals(oldRole, updatedRole);

        // legal role, but update nothing
        newRole = new UpdateRoleParam();
        oldRole = roleService.getById(id);
        roleService.updateRole(id, newRole);
        updatedRole = roleService.getById(id);
        log.debug("oldRole: " + oldRole);
        log.debug("updatedRole: " + updatedRole);
        assertEquals(oldRole.getId(), updatedRole.getId());
        assertEquals(oldRole.getName(), updatedRole.getName());
        assertEquals(oldRole.getDescription(), updatedRole.getDescription());
        assertEquals(oldRole.getStatus(), updatedRole.getStatus());
        assertEquals(oldRole.getSort(), updatedRole.getSort());
    }

    @Test
    void deleteRoles() {
        List<Long> ids;
        List<Role> oldRoles = roleService.list();

        // null
        ids = null;
        roleService.deleteRoles(ids);
        List<Role> updateRoles = roleService.list();
        assertEquals(oldRoles.size(), updateRoles.size());

        // empty
        ids = new ArrayList<>();
        oldRoles = roleService.list();
        roleService.deleteRoles(ids);
        updateRoles = roleService.list();
        assertEquals(oldRoles.size(), updateRoles.size());

        // not empty
        oldRoles = roleService.list();
        ids.add(oldRoles.get(0).getId());
        ids.add(oldRoles.get(1).getId());
        roleService.deleteRoles(ids);
        updateRoles = roleService.list();
        assertNotEquals(oldRoles.size(), updateRoles.size());
        log.debug("oldRoles: " + oldRoles);
        log.debug("updateRoles: " + updateRoles);
    }

    @Test
    void list() {
        Integer pageNum;
        Integer pageSize;
        String keyword;
        List<Role> allRoles = roleService.list();

        // keyword ""
        keyword = "";
        pageNum = 1;
        pageSize = 2;
        CommonPage<Role> result2 = roleService.list(keyword, pageSize, pageNum);
        log.debug("result2: " + result2);
        assertEquals(result2.getTotal(), Long.valueOf(allRoles.size()));
        assertEquals(pageSize, result2.getData().size());

        // empty result
        keyword = "asdqwesz";
        pageNum = 1;
        pageSize = 2;
        CommonPage<Role> result3 = roleService.list(keyword, pageSize, pageNum);
        assertEquals(0, result3.getData().size());

        // not empty result
        keyword = "管理员";
        pageNum = 1;
        pageSize = 2;
        CommonPage<Role> result4 = roleService.list(keyword, pageSize, pageNum);
        assertEquals(pageSize, result4.getData().size());
        log.debug("result4: " + result4);
    }

    @Test
    void listMenu() {
        Long id;

        // null
        id = null;
        List<Menu> result1 = roleService.listMenu(id);
        assertEquals(0, result1.size());

        // id not exit
        id = -1L;
        List<Menu> result2 = roleService.listMenu(id);
        assertEquals(0, result2.size());

        // id exit
        id = 1L;
        List<Menu> result3 = roleService.listMenu(id);
        assertNotEquals(0, result3.size());
        log.debug("result3: " + result3);
    }

    @Test
    void listResource() {
        Long id;

        // id null
        id = null;
        List<Resource> result1 = roleService.listResource(id);
        assertEquals(0, result1.size());

        // id not exit
        id = -1L;
        List<Resource> result2 = roleService.listResource(id);
        assertEquals(0, result2.size());

        // id exit
        id = 1L;
        List<Resource> result3 = roleService.listResource(id);
        assertNotEquals(0, result3.size());
        log.debug("result3: " + result3);
    }

    @Test
    void updateRoleMenu() {
        Long roleId;
        List<Long> menuIds;

        // illegal role
        roleId = null;
        menuIds = new ArrayList<>();
        Long finalRoleId = roleId;
        List<Long> finalMenuIds = menuIds;
        Throwable throwable1 = assertThrows(IllegalRoleException.class, () -> roleService.updateRoleMenu(finalRoleId, finalMenuIds));
        log.debug(throwable1.getMessage());

        // illegal menuIds
        roleId = 1L;
        menuIds = Arrays.asList(-1L, 2L);
        Long finalRoleId1 = roleId;
        List<Long> finalMenuIds1 = menuIds;
        Throwable throwable2 = assertThrows(IllegalMenuException.class, () ->roleService.updateRoleMenu(finalRoleId1, finalMenuIds1));
        log.debug(throwable2.getMessage());

        // menuIds null
        roleId = 1L;
        menuIds = null;
        List<Menu> oldMenus = roleService.listMenu(roleId);
        roleService.updateRoleMenu(roleId, menuIds);
        List<Menu> newMenus = roleService.listMenu(roleId);
        assertEquals(oldMenus, newMenus);
        log.debug("oldMenus: " + oldMenus);
        log.debug("newMenus: " + newMenus);

        // menuIds empty
        roleId = 1L;
        menuIds = new ArrayList<>();
        roleService.updateRoleMenu(roleId, menuIds);
        newMenus = roleService.listMenu(roleId);
        assertEquals(menuIds.size(), newMenus.size());
        log.debug("newMenus: " + newMenus);

        // menuIds not empty
        roleId = 1L;
        menuIds = menuService.list().stream().map(Base::getId).collect(Collectors.toList());
        roleService.updateRoleMenu(roleId, menuIds);
        newMenus = roleService.listMenu(roleId);
        assertEquals(menuIds.size(), newMenus.size());
        log.debug("newMenus: " + newMenus);
    }

    @Test
    void updateRoleResource() {
        Long roleId;
        List<Long> resourceIds;

        // illegal roleId
        roleId = null;
        resourceIds = new ArrayList<>();
        Long finalRoleId = roleId;
        List<Long> finalResourceIds = resourceIds;
        Throwable throwable1 = assertThrows(IllegalRoleException.class, () -> roleService.updateRoleResource(finalRoleId, finalResourceIds));
        log.debug(throwable1.getMessage());

        // illegal resourceIds
        roleId = 1L;
        resourceIds = Arrays.asList(-1L, -2L);
        Long finalRoleId1 = roleId;
        List<Long> finalResourceIds1 = resourceIds;
        Throwable throwable2 = assertThrows(IllegalResourceException.class, () -> roleService.updateRoleResource(finalRoleId1, finalResourceIds1));
        log.debug(throwable2.getMessage());

        // resourceIds null
        roleId = 1L;
        resourceIds = null;
        List<Resource> oldResources = roleService.listResource(roleId);
        roleService.updateRoleResource(roleId, resourceIds);
        List<Resource> newResources = roleService.listResource(roleId);
        assertEquals(oldResources, newResources);
        log.debug("oldResources: " + oldResources);
        log.debug("newResources: " + newResources);

        // resourceIds empty
        roleId = 1L;
        resourceIds = new ArrayList<>();
        roleService.updateRoleResource(roleId, resourceIds);
        newResources = roleService.listResource(roleId);
        assertEquals(resourceIds.size(), newResources.size());
        log.debug("newResources: " + newResources);

        // resourceIds not empty
        roleId = 1L;
        resourceIds = resourceService.list().stream().map(Base::getId).collect(Collectors.toList());
        roleService.updateRoleResource(roleId, resourceIds);
        newResources = roleService.listResource(roleId);
        assertEquals(resourceIds.size(), newResources.size());
        log.debug("newResources: " + newResources);
    }

    @Test
    void listenDeleteAdminEvent() {
        // admin not null
        Long id = 1L;
        Admin admin = adminService.getById(id);
        List<Role> oldRoles = roleService.getRoleListByAdminId(admin.getId());
        applicationEventPublisher.publishEvent(new DeleteAdminEvent(admin));
        List<Role> newRoles = roleService.getRoleListByAdminId(id);
        assertEquals(0, newRoles.size());
        log.debug("oldRoles: " + oldRoles);
        log.debug("newRoles: " + newRoles);

        // admin null
        id = -1L;
        admin = adminService.getById(id);
        Admin finalAdmin = admin;
        assertThrows(NullPointerException.class, () -> roleService.getRoleListByAdminId(finalAdmin.getId()));
    }
}