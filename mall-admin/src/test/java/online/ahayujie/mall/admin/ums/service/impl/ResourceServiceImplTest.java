package online.ahayujie.mall.admin.ums.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.ums.bean.dto.CreateResourceParam;
import online.ahayujie.mall.admin.ums.bean.dto.UpdateResourceParam;
import online.ahayujie.mall.admin.ums.bean.model.Resource;
import online.ahayujie.mall.admin.ums.exception.IllegalResourceCategoryException;
import online.ahayujie.mall.admin.ums.service.ResourceCategoryService;
import online.ahayujie.mall.admin.ums.service.ResourceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class ResourceServiceImplTest {
    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ResourceCategoryService resourceCategoryService;

    @Test
    void getResourceListByAdminId() {
        List<Long> roleIds;

        // null
        roleIds = null;
        List<Long> finalRoleIds = roleIds;
        assertThrows(NullPointerException.class, () -> resourceService.getResourceListByRoleIds(finalRoleIds));

        // empty
        roleIds = new ArrayList<>();
        List<Resource> result2 = resourceService.getResourceListByRoleIds(roleIds);
        assertEquals(0, result2.size());

        // not empty
        roleIds = Arrays.asList(1L, 2L);
        List<Resource> result3 = resourceService.getResourceListByRoleIds(roleIds);
        assertNotEquals(0, result3.size());
        log.debug(result3.toString());
        // 检查是否存在重复 resource
        Set<Resource> resourceSet = new HashSet<>(result3);
        assertEquals(resourceSet.size(), result3.size());
    }

    @Test
    void createResource() {
        CreateResourceParam resource;

        // illegal resource category
        resource = new CreateResourceParam();
        resource.setCategoryId(-1L);
        Throwable throwable1 = assertThrows(IllegalResourceCategoryException.class, () -> resourceService.createResource(resource));
        log.debug(throwable1.getMessage());

        // legal
        resource.setName("test");
        resource.setUrl("/test/**");
        resource.setDescription("setDescription");
        resource.setCategoryId(resourceCategoryService.listAll().get(0).getId());
        List<Resource> oldResources = resourceService.list();
        resourceService.createResource(resource);
        List<Resource> newResources = resourceService.list();
        assertEquals(oldResources.size() + 1, newResources.size());
        log.debug("oldResources: " + oldResources);
        log.debug("newResources: " + newResources);
    }

    @Test
    void updateResource() {
        UpdateResourceParam resource;

        // illegal resource category
        Long id = 1L;
        resource = new UpdateResourceParam();
        resource.setCategoryId(-1L);
        Long finalId = id;
        Throwable throwable1 = assertThrows(IllegalResourceCategoryException.class, () -> resourceService.updateResource(finalId, resource));
        log.debug(throwable1.getMessage());

        // legal
        resource.setName("update name");
        id = resourceService.list().get(0).getId();
        resource.setUrl("update url");
        resource.setDescription("update description");
        resource.setCategoryId(resourceCategoryService.listAll().get(0).getId());
        Resource oldResource = resourceService.getById(id);
        resourceService.updateResource(id, resource);
        Resource newResource = resourceService.getById(id);
        assertNotEquals(oldResource, newResource);
        log.debug("oldResource: " + oldResource);
        log.debug("newResource: " + newResource);
    }
}