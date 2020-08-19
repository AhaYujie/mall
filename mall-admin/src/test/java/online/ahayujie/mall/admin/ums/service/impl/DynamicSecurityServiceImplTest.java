package online.ahayujie.mall.admin.ums.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.ums.bean.dto.AdminLoginParam;
import online.ahayujie.mall.admin.ums.bean.dto.CreateResourceParam;
import online.ahayujie.mall.admin.ums.bean.model.*;
import online.ahayujie.mall.admin.ums.mapper.*;
import online.ahayujie.mall.admin.ums.service.AdminService;
import online.ahayujie.mall.admin.ums.service.ResourceService;
import online.ahayujie.mall.security.component.DynamicSecurityService;
import online.ahayujie.mall.security.jwt.TokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private AdminRoleRelationMapper adminRoleRelationMapper;

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private RoleResourceRelationMapper roleResourceRelationMapper;

    @Test
    void getAttributes() {
        String path = null;
        CreateResourceParam resource = new CreateResourceParam();
        resource.setName("test");
        resource.setUrl("/test/**");
        resourceService.createResource(resource);
        CreateResourceParam resource1 = new CreateResourceParam();
        resource1.setName("test1");
        resource1.setUrl("/test/test2");
        resourceService.createResource(resource1);

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
        Admin admin = new Admin();
        admin.setUsername(getRandomString(16));
        String password = getRandomString(16);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setStatus(Admin.ACTIVE_STATUS);
        adminMapper.insert(admin);
        loginParam.setUsername(admin.getUsername());
        loginParam.setPassword(password);
        String accessToken = adminService.login(loginParam).getAccessToken();
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        Collection<ConfigAttribute> configAttributes = dynamicSecurityService.getAttributes(authentication);
        assertEquals(0, configAttributes.size());

        // 有角色的用户
        List<Role> roles = new ArrayList<>();
        int size = 0;
        for (int i = 0; i < 3; i++) {
            Role role = new Role();
            role.setName("for test: " + i);
            role.setStatus(Role.STATUS.ACTIVE.getValue());
            roleMapper.insert(role);
            List<Resource> resources = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                Resource resource = new Resource();
                resource.setName("resource: " + i + " " + j);
                resource.setUrl("/" + i + "/" + j);
                resourceMapper.insert(resource);
                resources.add(resource);
            }
            List<RoleResourceRelation> relations = resources.stream()
                    .map(resource -> {
                        RoleResourceRelation relation = new RoleResourceRelation();
                        relation.setRoleId(role.getId());
                        relation.setResourceId(resource.getId());
                        return relation;
                    }).collect(Collectors.toList());
            roleResourceRelationMapper.insertList(relations);
            roles.add(role);
            size += resources.size();
        }
        List<AdminRoleRelation> relations = roles.stream()
                .map(role -> {
                    AdminRoleRelation relation = new AdminRoleRelation();
                    relation.setRoleId(role.getId());
                    relation.setAdminId(admin.getId());
                    return relation;
                }).collect(Collectors.toList());
        adminRoleRelationMapper.insertList(relations);
        loginParam.setUsername(admin.getUsername());
        loginParam.setPassword(password);
        accessToken = adminService.login(loginParam).getAccessToken();
        authentication = tokenProvider.getAuthentication(accessToken);
        configAttributes = dynamicSecurityService.getAttributes(authentication);
        assertEquals(size, configAttributes.size());
        log.debug("configAttributes: " + configAttributes);
    }

    @Test
    void getAllConfigAttributes() {
        List<Resource> resources = resourceService.list();
        Collection<ConfigAttribute> configAttributes = dynamicSecurityService.getAllConfigAttributes();
        assertEquals(resources.size(), configAttributes.size());
        log.debug("configAttributes: " + configAttributes);
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