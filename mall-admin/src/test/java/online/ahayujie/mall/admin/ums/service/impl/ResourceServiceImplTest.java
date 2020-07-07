package online.ahayujie.mall.admin.ums.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.ums.bean.dto.CreateResourceParam;
import online.ahayujie.mall.admin.ums.bean.dto.UpdateResourceParam;
import online.ahayujie.mall.admin.ums.bean.model.Resource;
import online.ahayujie.mall.admin.ums.bean.model.ResourceCategory;
import online.ahayujie.mall.admin.ums.exception.IllegalResourceCategoryException;
import online.ahayujie.mall.admin.ums.mapper.ResourceCategoryMapper;
import online.ahayujie.mall.admin.ums.mapper.ResourceMapper;
import online.ahayujie.mall.admin.ums.service.ResourceCategoryService;
import online.ahayujie.mall.admin.ums.service.ResourceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class ResourceServiceImplTest {
    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private ResourceCategoryMapper resourceCategoryMapper;

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
        CreateResourceParam finalResource = resource;
        Throwable throwable1 = assertThrows(IllegalResourceCategoryException.class, () -> resourceService.createResource(finalResource));
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

        // legal, null categoryId
        resource = new CreateResourceParam();
        resource.setName("test2");
        resource.setUrl("/test2/**");
        resource.setDescription("setDescription2");
        oldResources = resourceService.list();
        resourceService.createResource(resource);
        newResources = resourceService.list();
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

    @Test
    void removeById() {
        // exist
        Resource resource = new Resource();
        resource.setName("test resource");
        resource.setUrl("test url");
        resourceMapper.insert(resource);
        List<Resource> oldResources = resourceService.list();
        resourceService.removeById(resource.getId());
        List<Resource> newResources = resourceService.list();
        log.debug("oldResources: " + oldResources);
        log.debug("newResources: " + newResources);
        assertEquals(oldResources.size() - 1, newResources.size());

        // null
        Long id = null;
        oldResources = resourceService.list();
        resourceService.removeById(id);
        newResources = resourceService.list();
        log.debug("oldResources: " + oldResources);
        log.debug("newResources: " + newResources);
        assertEquals(oldResources.size(), newResources.size());

        // not exist
        id = -1L;
        oldResources = resourceService.list();
        resourceService.removeById(id);
        newResources = resourceService.list();
        log.debug("oldResources: " + oldResources);
        log.debug("newResources: " + newResources);
        assertEquals(oldResources.size(), newResources.size());
    }

    @Test
    void getByCategoryId() {
        // not exist category
        Long categoryId = null;
        List<Resource> result1 = resourceService.getByCategoryId(categoryId);
        assertEquals(0, result1.size());

        // exist category
        ResourceCategory resourceCategory = new ResourceCategory();
        resourceCategory.setName("new category");
        resourceCategory.setSort(0);
        resourceCategory.setCreateTime(new Date());
        resourceCategoryMapper.insert(resourceCategory);
        int size = new Random().nextInt(20) + 1;
        List<CreateResourceParam> params = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            CreateResourceParam param = new CreateResourceParam();
            param.setName("test");
            param.setUrl("test");
            param.setDescription("test");
            param.setCategoryId(resourceCategory.getId());
            params.add(param);
        }
        params.forEach(resourceService::createResource);
        List<Resource> resources = resourceService.getByCategoryId(resourceCategory.getId());
        log.debug("resources: " + resources);
        assertEquals(size, resources.size());
    }
}