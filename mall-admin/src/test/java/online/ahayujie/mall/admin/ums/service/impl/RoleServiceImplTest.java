package online.ahayujie.mall.admin.ums.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.ums.bean.model.Role;
import online.ahayujie.mall.admin.ums.service.RoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class RoleServiceImplTest {
    @Autowired
    private RoleService roleService;

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
}