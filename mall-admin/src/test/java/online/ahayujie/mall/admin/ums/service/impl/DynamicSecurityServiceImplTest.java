package online.ahayujie.mall.admin.ums.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.ums.bean.dto.AdminLoginParam;
import online.ahayujie.mall.admin.ums.bean.model.Resource;
import online.ahayujie.mall.admin.ums.service.AdminService;
import online.ahayujie.mall.admin.ums.service.ResourceService;
import online.ahayujie.mall.security.component.DynamicSecurityService;
import online.ahayujie.mall.security.jwt.TokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class DynamicSecurityServiceImplTest {
    @Autowired
    private AdminService adminService;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private DynamicSecurityService dynamicSecurityService;

    @Test
    void getAttributes() {
        String path = null;
        Resource resource = new Resource();
        resource.setName("test");
        resource.setUrl("/test/**");
        resourceService.save(resource);
        Resource resource1 = new Resource();
        resource1.setName("test1");
        resource1.setUrl("/test/test2");
        resourceService.save(resource1);

        // null
        Collection<ConfigAttribute> configAttributes = dynamicSecurityService.getAttributes(path);
        assertEquals(0, configAttributes.size());

        // not null
        path = "/test/aha";
        configAttributes = dynamicSecurityService.getAttributes(path);
        assertEquals(1, configAttributes.size());
        log.debug("configAttributes: " + configAttributes);

        path = "/test/test2";
        configAttributes = dynamicSecurityService.getAttributes(path);
        assertEquals(2, configAttributes.size());
        log.debug("configAttributes: " + configAttributes);
    }

    @Test
    void testGetAttributes() {
        AdminLoginParam loginParam = new AdminLoginParam();

        // 没有角色的用户
        loginParam.setUsername("test2");
        loginParam.setPassword("123456");
        String accessToken = adminService.login(loginParam).getAccessToken();
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        Collection<ConfigAttribute> configAttributes = dynamicSecurityService.getAttributes(authentication);
        assertEquals(0, configAttributes.size());

        // 有角色的用户
        loginParam.setUsername("test");
        loginParam.setPassword("123456");
        accessToken = adminService.login(loginParam).getAccessToken();
        authentication = tokenProvider.getAuthentication(accessToken);
        configAttributes = dynamicSecurityService.getAttributes(authentication);
        assertNotEquals(0, configAttributes.size());
        log.debug("configAttributes: " + configAttributes);
    }

    @Test
    void getAllConfigAttributes() {
        List<Resource> resources = resourceService.list();
        Collection<ConfigAttribute> configAttributes = dynamicSecurityService.getAllConfigAttributes();
        assertEquals(resources.size(), configAttributes.size());
        log.debug("configAttributes: " + configAttributes);
    }
}