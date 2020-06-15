package online.ahayujie.mall.admin.ums.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.ums.bean.dto.AdminLoginDTO;
import online.ahayujie.mall.admin.ums.bean.dto.AdminLoginParam;
import online.ahayujie.mall.admin.ums.bean.dto.AdminRegisterParam;
import online.ahayujie.mall.admin.ums.bean.model.Admin;
import online.ahayujie.mall.admin.ums.bean.model.Role;
import online.ahayujie.mall.admin.ums.exception.admin.DuplicateUsernameException;
import online.ahayujie.mall.admin.ums.service.AdminService;
import online.ahayujie.mall.admin.ums.service.RoleService;
import online.ahayujie.mall.common.bean.model.Base;
import online.ahayujie.mall.security.jwt.TokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
        admin.setUsername(getRandomString(random.nextInt(16)));
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
        throwable = assertThrows(IllegalArgumentException.class, () -> adminService.updateRole(finalAdminId1, finalRoleIds1));
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
}