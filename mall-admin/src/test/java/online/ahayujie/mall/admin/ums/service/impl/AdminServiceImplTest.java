package online.ahayujie.mall.admin.ums.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.ums.bean.dto.*;
import online.ahayujie.mall.admin.ums.bean.model.*;
import online.ahayujie.mall.admin.ums.exception.DuplicateUsernameException;
import online.ahayujie.mall.admin.ums.exception.IllegalAdminStatusException;
import online.ahayujie.mall.admin.ums.exception.IllegalRoleException;
import online.ahayujie.mall.admin.ums.mapper.*;
import online.ahayujie.mall.admin.ums.service.AdminService;
import online.ahayujie.mall.admin.ums.service.RoleService;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.common.bean.model.Base;
import online.ahayujie.mall.security.jwt.TokenProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class AdminServiceImplTest {
    private static Admin admin;

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private AdminRoleRelationMapper adminRoleRelationMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMenuRelationMapper roleMenuRelationMapper;

    @Value("${jwt.header}")
    private String JWT_HEADER;
    @Value("${jwt.header-prefix}")
    private String JWT_HEADER_PREFIX;

    @BeforeAll
    static void beforeAll() {
        Random random = new Random();
        admin = new Admin();
        admin.setUsername(getRandomString(random.nextInt(20) + 20));
        admin.setPassword("123456");
        admin.setEmail("1234567@qq.com");
        admin.setNickName("aha");
        admin.setNote("备注");
    }

    @Test
    void register() {
        // 正常注册
        AdminRegisterParam param = new AdminRegisterParam();
        param.setUsername(admin.getUsername());
        param.setPassword(admin.getPassword());
        param.setEmail(admin.getEmail());
        param.setNickName(admin.getNickName());
        param.setNote(admin.getNote());
        Admin result = adminService.register(param);
        assertNotNull(admin);
        log.debug(result.toString());
        assertEquals(admin.getUsername(), result.getUsername());
        assertNotNull(result.getPassword());
        assertNotEquals(admin.getPassword(), result.getPassword());
        assertEquals(admin.getEmail(), result.getEmail());
        assertEquals(admin.getNickName(), result.getNickName());
        assertEquals(admin.getNote(), result.getNote());
        assertEquals(Admin.ACTIVE_STATUS, result.getStatus());

        // 重复用户名
        assertThrows(DuplicateUsernameException.class, () -> adminService.register(param));
    }

    @Test
    void login() {
        admin.setUsername(getRandomString(40));
        AdminRegisterParam adminRegisterParam = new AdminRegisterParam();
        adminRegisterParam.setUsername(admin.getUsername());
        adminRegisterParam.setPassword(admin.getPassword());
        adminRegisterParam.setEmail(admin.getEmail());
        adminRegisterParam.setNickName(admin.getNickName());
        adminRegisterParam.setNote(admin.getNote());
        adminService.register(adminRegisterParam);
        // 正常登录
        AdminLoginParam adminLoginParam = new AdminLoginParam();
        adminLoginParam.setUsername(admin.getUsername());
        adminLoginParam.setPassword(admin.getPassword());
        AdminLoginDTO adminLoginDTO = adminService.login(adminLoginParam);
        assertNotNull(adminLoginDTO);
        log.debug(adminLoginDTO.toString());
        assertTrue(tokenProvider.validateAccessToken(adminLoginDTO.getAccessToken()));
        assertTrue(tokenProvider.validateRefreshToken(adminLoginDTO.getRefreshToken()));
        assertEquals(tokenProvider.getAccessTokenValidityInSeconds(), adminLoginDTO.getExpireIn());

        // 错误密码
        adminLoginParam.setPassword("");
        Throwable throwable = assertThrows(BadCredentialsException.class, () -> adminService.login(adminLoginParam));
        log.debug(throwable.getMessage());

        // 用户名不存在
        Random random = new Random();
        adminLoginParam.setUsername(getRandomString(random.nextInt(16)));
        throwable = assertThrows(UsernameNotFoundException.class, () -> adminService.login(adminLoginParam));
        log.debug(throwable.getMessage());

        // 用户被禁用
        Admin admin = new Admin();
        admin.setUsername(getRandomString(16));
        String password = getRandomString(16);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setStatus(Admin.UN_ACTIVE_STATUS);
        adminMapper.insert(admin);
        AdminLoginParam param = new AdminLoginParam();
        param.setUsername(admin.getUsername());
        param.setPassword(password);
        Throwable throwable1 = assertThrows(DisabledException.class, () -> adminService.login(param));
        log.debug(throwable1.getMessage());
    }

    @Test
    void refreshToken() throws InterruptedException {
        String refreshToken = null;

        // null
        Throwable throwable = assertThrows(IllegalArgumentException.class, () -> adminService.refreshAccessToken(refreshToken));
        log.debug(throwable.getMessage());

        // legal refreshToken
        Admin admin = new Admin();
        admin.setUsername(getRandomString(16));
        String password = getRandomString(16);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setStatus(Admin.ACTIVE_STATUS);
        adminMapper.insert(admin);
        AdminLoginParam param = new AdminLoginParam();
        param.setUsername(admin.getUsername());
        param.setPassword(password);
        AdminLoginDTO adminLoginDTO = adminService.login(param);
        Thread.sleep(2000);
        AdminLoginDTO adminLoginDTO1 = adminService.refreshAccessToken(adminLoginDTO.getRefreshToken());
        assertNotNull(adminLoginDTO1.getAccessToken());
        assertEquals(adminLoginDTO.getRefreshToken(), adminLoginDTO1.getRefreshToken());
        assertNotEquals(adminLoginDTO.getAccessToken(), adminLoginDTO1.getAccessToken());
        log.debug("adminLoginDTO: " + adminLoginDTO1);
    }

    @Test
    void updateRole() {
        Long adminId = null;
        List<Long> roleIds = new ArrayList<>();

        // adminId is null
        Long finalAdminId = adminId;
        List<Long> finalRoleIds = roleIds;
        Throwable throwable = assertThrows(UsernameNotFoundException.class, () -> adminService.updateRole(finalAdminId, finalRoleIds));
        log.debug(throwable.getMessage());

        // 插入用户
        Admin admin = new Admin();
        admin.setUsername(getRandomString(16));
        String password = getRandomString(16);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setStatus(Admin.UN_ACTIVE_STATUS);
        adminMapper.insert(admin);
        // 插入角色
        List<Role> roleHave = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Role role = new Role();
            role.setName("have role: " + i);
            role.setStatus(Role.STATUS.ACTIVE.getValue());
            roleHave.add(role);
        }
        roleHave.forEach(roleMapper::insert);
        Role unActiveRole = new Role();
        unActiveRole.setName("un active role");
        unActiveRole.setStatus(Role.STATUS.UN_ACTIVE.getValue());
        roleMapper.insert(unActiveRole);
        // 插入用户角色关系
        List<AdminRoleRelation> relations = roleHave.stream()
                .map(role -> {
                    AdminRoleRelation relation = new AdminRoleRelation();
                    relation.setAdminId(admin.getId());
                    relation.setRoleId(role.getId());
                    return relation;
                }).collect(Collectors.toList());
        adminRoleRelationMapper.insertList(relations);

        // roleIds is null
        adminId = admin.getId();
        roleIds = null;
        List<Role> adminRoles = roleService.getRoleListByAdminId(adminId);
        adminService.updateRole(adminId, roleIds);
        List<Role> updateAdminRoles = roleService.getRoleListByAdminId(adminId);
        assertEquals(adminRoles.size(), updateAdminRoles.size());
        List<Long> updateAdminRoleIds = updateAdminRoles.stream().map(Base::getId).collect(Collectors.toList());
        for (Role role : adminRoles) {
            assertTrue(updateAdminRoleIds.contains(role.getId()));
        }

        // roleIds is empty
        roleIds = new ArrayList<>();
        adminService.updateRole(adminId, roleIds);
        updateAdminRoles = roleService.getRoleListByAdminId(adminId);
        assertEquals(0, updateAdminRoles.size());
        log.debug("roleIds is empty: " + updateAdminRoles);

        // roleIds is illegal
        roleIds = Arrays.asList(-1L, -2L);
        Long finalAdminId1 = adminId;
        List<Long> finalRoleIds1 = roleIds;
        throwable = assertThrows(IllegalRoleException.class, () -> adminService.updateRole(finalAdminId1, finalRoleIds1));
        log.debug(throwable.getMessage());
        roleIds = Collections.singletonList(unActiveRole.getId());
        Long finalAdminId2 = adminId;
        List<Long> finalRoleIds2 = roleIds;
        throwable = assertThrows(IllegalRoleException.class, () -> adminService.updateRole(finalAdminId2, finalRoleIds2));
        log.debug(throwable.getMessage());

        // roleIds is not empty
        roleIds = roleService.getActiveRoles().stream().map(Base::getId).collect(Collectors.toList());
        adminService.updateRole(adminId, roleIds);
        updateAdminRoles = roleService.getRoleListByAdminId(adminId);
        assertEquals(roleIds.size(), updateAdminRoles.size());
        for (Role role : updateAdminRoles) {
            assertTrue(roleIds.contains(role.getId()));
        }
        log.debug("roleIds is not empty: " + updateAdminRoles);
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

    @Test
    void getAdminList() {
        String keyword;
        int pageNum, pageSize;

        // not exist
        keyword = getRandomString(20);
        pageNum = 1;
        pageSize = 20;
        CommonPage<Admin> result1 = adminService.getAdminList(keyword, pageNum, pageSize);
        assertEquals(0, result1.getTotal());
        log.debug("result1: " + result1);

        // exist
        keyword = "";
        pageNum = 1;
        pageSize = 20;
        CommonPage<Admin> result2 = adminService.getAdminList(keyword, pageNum, pageSize);
        assertNotEquals(0, result2.getTotal());
        log.debug("result2: " + result2);

        String username = null;
        String nickname = getRandomString(16);
        for (int i = 0; i < 10; i++) {
            Admin admin = new Admin();
            username = getRandomString(5 + i);
            admin.setUsername(username);
            admin.setNickName(nickname);
            adminMapper.insert(admin);
        }
        CommonPage<Admin> result3 = adminService.getAdminList(username, 1, 5);
        assertEquals(1, result3.getData().size());
        CommonPage<Admin> result5 = adminService.getAdminList(nickname, 1, 5);
        assertEquals(5, result5.getData().size());
        CommonPage<Admin> result6 = adminService.getAdminList(nickname, 2, 5);
        assertEquals(5, result6.getData().size());
    }

    @Test
    void updateAdmin() {
        // 插入用户
        Admin admin = new Admin();
        admin.setUsername(getRandomString(16));
        adminMapper.insert(admin);

        long id;
        UpdateAdminParam param;

        // illegal status
        id = admin.getId();
        param = new UpdateAdminParam();
        param.setStatus(-1);
        Long finalId = id;
        UpdateAdminParam finalParam = param;
        Throwable throwable1 = assertThrows(IllegalAdminStatusException.class, () -> adminService.updateAdmin(finalId, finalParam));
        log.debug(throwable1.getMessage());

        // legal
        id = admin.getId();
        param = new UpdateAdminParam();
        param.setUsername(getRandomString(40));
        param.setPassword(getRandomString(16));
        param.setEmail("new email");
        param.setIcon("new icon");
        param.setNickName("new nickname");
        param.setNote("new note");
        param.setStatus(Admin.UN_ACTIVE_STATUS);
        Admin oldAdmin = adminService.getById(id);
        adminService.updateAdmin(id, param);
        Admin newAdmin = adminService.getById(id);
        assertNotEquals(oldAdmin, newAdmin);
        log.debug("oldAdmin: " + oldAdmin);
        log.debug("newAdmin: " + newAdmin);
        Admin compare = new Admin();
        BeanUtils.copyProperties(newAdmin, compare);
        BeanUtils.copyProperties(param, compare);
        compare.setPassword(newAdmin.getPassword());
        assertEquals(compare, newAdmin);
    }

    @Test
    void updatePassword() {
        UpdateAdminPasswordParam param;

        // not exist user
        param = new UpdateAdminPasswordParam();
        param.setUsername(getRandomString(20));
        param.setOldPassword(getRandomString(20));
        param.setNewPassword(getRandomString(20));
        UpdateAdminPasswordParam finalParam = param;
        Throwable throwable1 = assertThrows(UsernameNotFoundException.class, () -> adminService.updatePassword(finalParam));
        log.debug(throwable1.getMessage());

        // 插入用户
        Admin admin = new Admin();
        admin.setUsername(getRandomString(16));
        String password = getRandomString(16);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setStatus(Admin.ACTIVE_STATUS);
        adminMapper.insert(admin);

        // old password wrong
        param = new UpdateAdminPasswordParam();
        param.setUsername(admin.getUsername());
        param.setOldPassword(getRandomString(20));
        param.setNewPassword(getRandomString(20));
        UpdateAdminPasswordParam finalParam1 = param;
        Throwable throwable2 = assertThrows(BadCredentialsException.class, () -> adminService.updatePassword(finalParam1));
        log.debug(throwable2.getMessage());

        // legal
        String username = admin.getUsername();
        String newPassword = "newPassword";
        param = new UpdateAdminPasswordParam();
        param.setUsername(username);
        param.setOldPassword(password);
        param.setNewPassword(newPassword);
        adminService.updatePassword(param);
        AdminLoginParam loginParam = new AdminLoginParam();
        loginParam.setUsername(username);
        loginParam.setPassword(newPassword);
        AdminLoginDTO adminLoginDTO = adminService.login(loginParam);
        log.debug("adminLoginDTO: " + adminLoginDTO);
    }

    @Test
    void removeById() {
        // 删除存在的用户
        admin.setUsername(getRandomString(40));
        AdminRegisterParam adminRegisterParam = new AdminRegisterParam();
        adminRegisterParam.setUsername(admin.getUsername());
        adminRegisterParam.setPassword(admin.getPassword());
        adminRegisterParam.setEmail(admin.getEmail());
        adminRegisterParam.setNickName(admin.getNickName());
        adminRegisterParam.setNote(admin.getNote());
        adminService.register(adminRegisterParam);
        AdminLoginParam adminLoginParam = new AdminLoginParam();
        adminLoginParam.setUsername(admin.getUsername());
        adminLoginParam.setPassword(admin.getPassword());
        AdminLoginDTO adminLoginDTO = adminService.login(adminLoginParam);
        Admin admin = adminService.getAdminFromToken(adminLoginDTO.getAccessToken());
        log.debug("admin: " + admin);
        int count1 = adminService.removeById(admin.getId());
        assertThrows(UsernameNotFoundException.class, () -> adminService.login(adminLoginParam));
        log.debug("count1: " + count1);

        // 删除不存在的用户
        Long id = null;
        int count2 = adminService.removeById(id);
        assertEquals(0, count2);
    }

    @Test
    void getAdminFromToken() {
        // 插入用户和登录获取token
        Admin admin = new Admin();
        admin.setUsername(getRandomString(16));
        String password = getRandomString(16);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setStatus(Admin.ACTIVE_STATUS);
        adminMapper.insert(admin);
        AdminLoginParam param = new AdminLoginParam();
        param.setUsername(admin.getUsername());
        param.setPassword(password);
        AdminLoginDTO adminLoginDTO = adminService.login(param);

        // illegal
        Admin admin1 = adminService.getAdminFromToken(null);
        assertNull(admin1);

        // legal
        Admin admin2 = adminService.getAdminFromToken(adminLoginDTO.getAccessToken());
        assertNotNull(admin2);
        assertEquals(admin.getId(), admin2.getId());
        assertEquals(admin.getUsername(), admin2.getUsername());
        Admin admin3 = adminService.getAdminFromToken(adminLoginDTO.getRefreshToken());
        assertNotNull(admin3);
        assertEquals(admin.getId(), admin3.getId());
        assertEquals(admin.getUsername(), admin3.getUsername());
    }
    @Test
    void testGetAdminList() {
        List<Admin> admins = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Admin admin = new Admin();
            admin.setUsername(getRandomString(45));
            admins.add(admin);
        }
        admins.forEach(adminMapper::insert);
        CommonPage<Admin> result = adminService.getAdminList(1L, 5L);
        assertEquals(5, result.getData().size());
        CommonPage<Admin> result1 = adminService.getAdminList(2L, 5L);
        assertEquals(5, result1.getData().size());
    }

    @Test
    void getAdminInfo() {
        // 登录
        Random random = new Random();
        String password = "123456";
        Admin admin = new Admin();
        admin.setUsername(getRandomString(random.nextInt(10) + 6));
        admin.setPassword(passwordEncoder.encode(password));
        admin.setStatus(Admin.ACTIVE_STATUS);
        adminMapper.insert(admin);
        admin.setPassword(password);
        AdminLoginParam loginParam = new AdminLoginParam();
        loginParam.setUsername(admin.getUsername());
        loginParam.setPassword(admin.getPassword());
        AdminLoginDTO loginDTO = adminService.login(loginParam);
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assertNotNull(servletRequestAttributes);
        HttpServletRequest request = servletRequestAttributes.getRequest();
        MockHttpServletRequest mockHttpServletRequest = (MockHttpServletRequest) request;
        mockHttpServletRequest.addHeader(JWT_HEADER, JWT_HEADER_PREFIX + loginDTO.getAccessToken());

        // 创建角色
        List<Role> roles = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Role role = new Role();
            role.setName(getRandomString(5));
            role.setStatus(Role.STATUS.ACTIVE.getValue());
            roleMapper.insert(role);
            roles.add(role);
        }
        for (Role role : roles) {
            AdminRoleRelation relation = new AdminRoleRelation();
            relation.setAdminId(admin.getId());
            relation.setRoleId(role.getId());
            adminRoleRelationMapper.insert(relation);
        }
        // 创建菜单
        List<Menu> menus = new ArrayList<>();
        Menu commonMenu = new Menu();
        commonMenu.setName(getRandomString(5));
        menuMapper.insert(commonMenu);
        menus.add(commonMenu);
        for (Role role : roles) {
            Menu menu = new Menu();
            menu.setName(getRandomString(5));
            menuMapper.insert(menu);
            menus.add(menu);
            RoleMenuRelation relation = new RoleMenuRelation();
            relation.setRoleId(role.getId());
            relation.setMenuId(menu.getId());
            roleMenuRelationMapper.insert(relation);
            relation = new RoleMenuRelation();
            relation.setRoleId(role.getId());
            relation.setMenuId(commonMenu.getId());
            roleMenuRelationMapper.insert(relation);
        }

        AdminInfoDTO adminInfoDTO = adminService.getAdminInfo();
        assertNotNull(adminInfoDTO);
        assertEquals(admin.getUsername(), adminInfoDTO.getUsername());
        assertEquals(roles.size(), adminInfoDTO.getRoles().size());
        for (Role role : roles) {
            boolean exist = false;
            for (Role compare : adminInfoDTO.getRoles()) {
                exist = (exist || compare.getName().equals(role.getName()));
            }
            assertTrue(exist);
        }
        assertEquals(menus.size(), adminInfoDTO.getMenus().size());
        for (Menu menu : menus) {
            boolean exist = false;
            for (Menu compare : adminInfoDTO.getMenus()) {
                exist = (exist || compare.getName().equals(menu.getName()));
            }
            assertTrue(exist);
        }
    }
}