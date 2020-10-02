package online.ahayujie.mall.admin.ums.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.ums.bean.dto.*;
import online.ahayujie.mall.admin.ums.bean.model.*;
import online.ahayujie.mall.admin.ums.exception.IllegalMenuException;
import online.ahayujie.mall.admin.ums.exception.IllegalResourceException;
import online.ahayujie.mall.admin.ums.exception.IllegalRoleException;
import online.ahayujie.mall.admin.ums.mapper.*;
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

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class RoleServiceImplTest {
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private AdminRoleRelationMapper adminRoleRelationMapper;

    @Autowired
    private RoleMenuRelationMapper roleMenuRelationMapper;

    @Autowired
    private RoleResourceRelationMapper roleResourceRelationMapper;

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

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private ResourceMapper resourceMapper;

    @Test
    void getRoleListByAdminId() {
        Long adminId = null;
        // 用 null 查询不存在的用户
        List<Role> result1 = roleService.getRoleListByAdminId(adminId);
        assertEquals(0, result1.size());

        // 查询存在的用户但没有角色
        // 创建用户
        Admin admin = new Admin();
        admin.setUsername(getRandomString(16));
        adminMapper.insert(admin);
        adminId = admin.getId();
        List<Role> result2 = roleService.getRoleListByAdminId(adminId);
        assertEquals(0, result2.size());

        // 查询存在的用户且拥有角色
        // 创建角色
        List<Role> roles = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Role role = new Role();
            role.setName("for test: " + i);
            role.setStatus(Role.STATUS.ACTIVE.getValue());
            roleMapper.insert(role);
            roles.add(role);
        }
        // 用户角色关系
        List<AdminRoleRelation> relations = roles.stream()
                .map(role -> {
                    AdminRoleRelation relation = new AdminRoleRelation();
                    relation.setAdminId(admin.getId());
                    relation.setRoleId(role.getId());
                    return relation;
                }).collect(Collectors.toList());
        adminRoleRelationMapper.insertList(relations);
        adminId = admin.getId();
        List<Role> result3 = roleService.getRoleListByAdminId(adminId);
        assertEquals(roles.size(), result3.size());
        for (Role role : result3) {
            assertEquals(Role.STATUS.ACTIVE.getValue(), role.getStatus());
        }
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
        Role role = new Role();
        role.setName("for test");
        roleMapper.insert(role);
        Long id = role.getId();
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
        List<Role> roles = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Role role = new Role();
            role.setName("for test: " + i);
            roles.add(role);
        }
        roles.forEach(roleMapper::insert);
        oldRoles = roleService.list();
        ids = roles.stream().map(Base::getId).collect(Collectors.toList());
        roleService.deleteRoles(ids);
        updateRoles = roleService.list();
        assertNotEquals(oldRoles.size(), updateRoles.size());
        log.debug("oldRoles: " + oldRoles);
        log.debug("updateRoles: " + updateRoles);

        // test relationship
        List<Role> testRoles = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < random.nextInt(20) + 1; i++) {
            Role role = new Role();
            role.setName("test role");
            role.setDescription("for test");
            role.setStatus(Role.STATUS.ACTIVE.getValue());
            role.setSort(0);
            role.setCreateTime(new Date());
            testRoles.add(role);
        }
        testRoles.forEach(roleMapper::insert);
        List<AdminRoleRelation> adminRoleRelations = new ArrayList<>();
        for (int i = 0; i < random.nextInt(20) + 1; i++) {
            AdminRoleRelation relation = new AdminRoleRelation();
            relation.setRoleId(testRoles.get(random.nextInt(testRoles.size())).getId());
            relation.setAdminId((long) i);
            relation.setCreateTime(new Date());
            adminRoleRelations.add(relation);
        }
        adminRoleRelationMapper.insertList(adminRoleRelations);
        List<RoleMenuRelation> roleMenuRelations = new ArrayList<>();
        for (int i = 0; i < random.nextInt(20) + 1; i++) {
            RoleMenuRelation relation = new RoleMenuRelation();
            relation.setRoleId(testRoles.get(random.nextInt(testRoles.size())).getId());
            relation.setMenuId((long) i);
            relation.setCreateTime(new Date());
            roleMenuRelations.add(relation);
        }
        roleMenuRelationMapper.insertList(roleMenuRelations);
        List<RoleResourceRelation> roleResourceRelations = new ArrayList<>();
        for (int i = 0; i < random.nextInt(20) + 1; i++) {
            RoleResourceRelation relation = new RoleResourceRelation();
            relation.setRoleId(testRoles.get(random.nextInt(testRoles.size())).getId());
            relation.setResourceId((long) i);
            relation.setCreateTime(new Date());
            roleResourceRelations.add(relation);
        }
        roleResourceRelationMapper.insertList(roleResourceRelations);
        List<Long> roleIds = testRoles.stream().map(Base::getId).collect(Collectors.toList());
        roleService.deleteRoles(roleIds);
        for (Long roleId : roleIds) {
            assertEquals(0, adminRoleRelationMapper.selectByRoleId(roleId).size());
            assertEquals(0, roleMenuRelationMapper.selectByRoleId(roleId).size());
            assertEquals(0, roleResourceRelationMapper.selectByRoleId(roleId).size());
        }
    }

    @Test
    void list() {
        for (int i = 0; i < 10; i++) {
            Role role = new Role();
            role.setName("for test: " + i);
            roleMapper.insert(role);
        }
        int pageNum;
        int pageSize;
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
        for (int i = 0; i < 10; i++) {
            Role role = new Role();
            role.setName("role for test");
            roleMapper.insert(role);
        }
        keyword = "role for test";
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
        Role role = new Role();
        role.setName("for test");
        roleMapper.insert(role);
        List<Menu> menus = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Menu menu = new Menu();
            menu.setName("menu:" + i);
            menu.setParentId(Menu.NON_PARENT_ID);
            menus.add(menu);
        }
        menus.forEach(menuMapper::insert);
        List<RoleMenuRelation> relations = menus.stream()
                .map(menu -> {
                    RoleMenuRelation relation = new RoleMenuRelation();
                    relation.setRoleId(role.getId());
                    relation.setMenuId(menu.getId());
                    return relation;
                }).collect(Collectors.toList());
        roleMenuRelationMapper.insertList(relations);
        id = role.getId();
        List<Menu> result3 = roleService.listMenu(id);
        assertEquals(menus.size(), result3.size());
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
        Role role = new Role();
        role.setName("for test");
        roleMapper.insert(role);
        List<Resource> resources = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Resource resource = new Resource();
            resource.setName("for test: " + i);
            resources.add(resource);
        }
        resources.forEach(resourceMapper::insert);
        List<RoleResourceRelation> relations = resources.stream()
                .map(resource -> {
                    RoleResourceRelation relation = new RoleResourceRelation();
                    relation.setRoleId(role.getId());
                    relation.setResourceId(relation.getRoleId());
                    return relation;
                }).collect(Collectors.toList());
        roleResourceRelationMapper.insertList(relations);
        id = role.getId();
        List<Resource> result3 = roleService.listResource(id);
        assertEquals(resources.size(), result3.size());
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

        Role role = new Role();
        role.setName("for test");
        roleMapper.insert(role);

        // illegal menuIds
        roleId = role.getId();
        menuIds = Arrays.asList(-1L, 2L);
        Long finalRoleId1 = roleId;
        List<Long> finalMenuIds1 = menuIds;
        Throwable throwable2 = assertThrows(IllegalMenuException.class, () ->roleService.updateRoleMenu(finalRoleId1, finalMenuIds1));
        log.debug(throwable2.getMessage());

        // menuIds null
        roleId = role.getId();
        menuIds = null;
        List<Menu> oldMenus = roleService.listMenu(roleId);
        roleService.updateRoleMenu(roleId, menuIds);
        List<Menu> newMenus = roleService.listMenu(roleId);
        assertEquals(oldMenus, newMenus);
        log.debug("oldMenus: " + oldMenus);
        log.debug("newMenus: " + newMenus);

        // menuIds empty
        roleId = role.getId();
        menuIds = new ArrayList<>();
        roleService.updateRoleMenu(roleId, menuIds);
        newMenus = roleService.listMenu(roleId);
        assertEquals(menuIds.size(), newMenus.size());
        log.debug("newMenus: " + newMenus);

        // menuIds not empty
        List<Menu> menus = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Menu menu = new Menu();
            menu.setName("for test: " + i);
            menu.setParentId(Menu.NON_PARENT_ID);
            menus.add(menu);
        }
        menus.forEach(menuMapper::insert);
        roleId = role.getId();
        menuIds = menus.stream().map(Base::getId).collect(Collectors.toList());
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

        Role role = new Role();
        role.setName("for test");
        roleMapper.insert(role);

        // illegal resourceIds
        roleId = role.getId();
        resourceIds = Arrays.asList(-1L, -2L);
        Long finalRoleId1 = roleId;
        List<Long> finalResourceIds1 = resourceIds;
        Throwable throwable2 = assertThrows(IllegalResourceException.class, () -> roleService.updateRoleResource(finalRoleId1, finalResourceIds1));
        log.debug(throwable2.getMessage());

        // resourceIds null
        roleId = role.getId();
        resourceIds = null;
        List<Resource> oldResources = roleService.listResource(roleId);
        roleService.updateRoleResource(roleId, resourceIds);
        List<Resource> newResources = roleService.listResource(roleId);
        assertEquals(oldResources, newResources);
        log.debug("oldResources: " + oldResources);
        log.debug("newResources: " + newResources);

        // resourceIds empty
        roleId = role.getId();
        resourceIds = new ArrayList<>();
        roleService.updateRoleResource(roleId, resourceIds);
        newResources = roleService.listResource(roleId);
        assertEquals(resourceIds.size(), newResources.size());
        log.debug("newResources: " + newResources);

        // resourceIds not empty
        List<Resource> resources = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Resource resource = new Resource();
            resource.setName("for test: " + i);
            resourceMapper.insert(resource);
            resources.add(resource);
        }
        roleId = role.getId();
        resourceIds = resources.stream().map(Base::getId).collect(Collectors.toList());
        roleService.updateRoleResource(roleId, resourceIds);
        newResources = roleService.listResource(roleId);
        assertEquals(resourceIds.size(), newResources.size());
        log.debug("newResources: " + newResources);
    }

    private static String getRandomString(int length) {
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}