package online.ahayujie.mall.admin.ums.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.ums.bean.dto.AdminLoginDTO;
import online.ahayujie.mall.admin.ums.bean.dto.AdminLoginParam;
import online.ahayujie.mall.admin.ums.bean.dto.AdminRegisterParam;
import online.ahayujie.mall.admin.ums.bean.model.Admin;
import online.ahayujie.mall.admin.ums.exception.admin.DuplicateUsernameException;
import online.ahayujie.mall.admin.ums.service.AdminService;
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

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class AdminServiceImplTest {
    private static Admin admin;

    @Autowired
    private AdminService adminService;

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