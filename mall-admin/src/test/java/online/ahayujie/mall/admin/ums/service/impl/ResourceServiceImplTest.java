package online.ahayujie.mall.admin.ums.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.ums.bean.model.Resource;
import online.ahayujie.mall.admin.ums.service.ResourceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class ResourceServiceImplTest {
    @Autowired
    private ResourceService resourceService;

    @Test
    void getResourceListByAdminId() {
        Long adminId = null;

        // 用 null 查询不存在的用户
        List<Resource> result1 = resourceService.getResourceListByAdminId(adminId);
        assertEquals(0, result1.size());

        // 查询存在的用户但没有 resource
        adminId = 2L;
        List<Resource> result2 = resourceService.getResourceListByAdminId(adminId);
        assertEquals(0, result2.size());

        // 查询存在的用户且有 resource
        adminId = 1L;
        List<Resource> result3 = resourceService.getResourceListByAdminId(adminId);
        assertNotEquals(0, result3.size());
        log.debug(result3.toString());
        // 检查是否存在重复 resource
        Set<Resource> resourceSet = new HashSet<>(result3);
        assertEquals(resourceSet.size(), result3.size());
    }
}