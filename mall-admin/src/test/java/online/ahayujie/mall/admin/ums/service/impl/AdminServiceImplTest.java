package online.ahayujie.mall.admin.ums.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.ums.bean.dto.*;
import online.ahayujie.mall.admin.ums.bean.model.Admin;
import online.ahayujie.mall.admin.ums.bean.model.Role;
import online.ahayujie.mall.admin.ums.exception.DuplicateUsernameException;
import online.ahayujie.mall.admin.ums.exception.IllegalAdminStatusException;
import online.ahayujie.mall.admin.ums.exception.IllegalRoleException;
import online.ahayujie.mall.admin.ums.service.AdminService;
import online.ahayujie.mall.admin.ums.service.RoleService;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.common.bean.model.Base;
import online.ahayujie.mall.security.jwt.TokenProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

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

    @BeforeAll
    static void beforeAll() {
        Random random = new Random();
        admin = new Admin();
        admin.setUsername(getRandomString(random.nextInt(16) + 1));
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

        // 重复用户名
        assertThrows(DuplicateUsernameException.class, () -> adminService.register(param));
    }

    @Test
    void login() {
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
    }

    @Test
    void refreshToken() throws InterruptedException {
        String refreshToken = null;

        // null
        Throwable throwable = assertThrows(IllegalArgumentException.class, () -> adminService.refreshAccessToken(refreshToken));
        log.debug(throwable.getMessage());

        // legal refreshToken
        AdminLoginParam param = new AdminLoginParam();
        param.setUsername("test");
        param.setPassword("123456");
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
        List<Role> roles = roleService.list();
        Long adminId = null;
        List<Long> roleIds = new ArrayList<>();

        // adminId is null
        Long finalAdminId = adminId;
        List<Long> finalRoleIds = roleIds;
        Throwable throwable = assertThrows(UsernameNotFoundException.class, () -> adminService.updateRole(finalAdminId, finalRoleIds));
        log.debug(throwable.getMessage());

        // roleIds is null
        adminId = 1L;
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
        roleIds = Arrays.asList(-1L, 1L, 2L);
        Long finalAdminId1 = adminId;
        List<Long> finalRoleIds1 = roleIds;
        throwable = assertThrows(IllegalRoleException.class, () -> adminService.updateRole(finalAdminId1, finalRoleIds1));
        log.debug(throwable.getMessage());

        // roleIds is not empty
        roleIds = roles.stream().map(Base::getId).collect(Collectors.toList());
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
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    @Test
    void getAdminList() {
        String keyword;
        Integer pageNum, pageSize;

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
    }

    @Test
    void updateAdmin() {
        Long id;
        UpdateAdminParam param;

        // illegal status
        id = 1L;
        param = new UpdateAdminParam();
        param.setStatus(-1);
        Long finalId = id;
        UpdateAdminParam finalParam = param;
        Throwable throwable1 = assertThrows(IllegalAdminStatusException.class, () -> adminService.updateAdmin(finalId, finalParam));
        log.debug(throwable1.getMessage());

        // legal
        id = 1L;
        param = new UpdateAdminParam();
        param.setUsername("new username");
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

        // old password wrong
        param = new UpdateAdminPasswordParam();
        param.setUsername("test");
        param.setOldPassword(getRandomString(20));
        param.setNewPassword(getRandomString(20));
        UpdateAdminPasswordParam finalParam1 = param;
        Throwable throwable2 = assertThrows(BadCredentialsException.class, () -> adminService.updatePassword(finalParam1));
        log.debug(throwable2.getMessage());

        // legal
        String username = "test";
        String newPassword = "newPassword";
        param = new UpdateAdminPasswordParam();
        param.setUsername(username);
        param.setOldPassword("123456");
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
}