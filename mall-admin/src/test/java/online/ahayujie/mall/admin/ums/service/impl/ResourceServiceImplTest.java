package online.ahayujie.mall.admin.ums.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.ums.bean.dto.CreateResourceParam;
import online.ahayujie.mall.admin.ums.bean.dto.UpdateResourceParam;
import online.ahayujie.mall.admin.ums.bean.model.Resource;
import online.ahayujie.mall.admin.ums.bean.model.ResourceCategory;
import online.ahayujie.mall.admin.ums.bean.model.Role;
import online.ahayujie.mall.admin.ums.bean.model.RoleResourceRelation;
import online.ahayujie.mall.admin.ums.exception.IllegalResourceCategoryException;
import online.ahayujie.mall.admin.ums.mapper.ResourceCategoryMapper;
import online.ahayujie.mall.admin.ums.mapper.ResourceMapper;
import online.ahayujie.mall.admin.ums.mapper.RoleMapper;
import online.ahayujie.mall.admin.ums.mapper.RoleResourceRelationMapper;
import online.ahayujie.mall.admin.ums.service.ResourceCategoryService;
import online.ahayujie.mall.admin.ums.service.ResourceService;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.common.bean.model.Base;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleResourceRelationMapper roleResourceRelationMapper;

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
        // 新增角色
        int resourceSize = 0;
        List<Role> roles = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Role role = new Role();
            role.setName("role:" + i);
            roleMapper.insert(role);
            // 角色的资源
            List<Resource> resources = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                Resource resource = new Resource();
                resource.setName("name:" + i + "/" + j);
                resource.setUrl("url:" + i + "/" + j);
                resourceMapper.insert(resource);
                resources.add(resource);
            }
            // 角色资源关系
            List<RoleResourceRelation> relations = resources.stream()
                    .map(resource -> {
                        RoleResourceRelation relation = new RoleResourceRelation();
                        relation.setRoleId(role.getId());
                        relation.setResourceId(resource.getId());
                        return relation;
                    }).collect(Collectors.toList());
            roleResourceRelationMapper.insertList(relations);
            roles.add(role);
            resourceSize += resources.size();
        }
        // 每个角色都拥有的资源
        Resource common = new Resource();
        common.setName("common");
        common.setUrl("common url");
        resourceMapper.insert(common);
        List<RoleResourceRelation> relations = roles.stream()
                .map(role -> {
                    RoleResourceRelation relation = new RoleResourceRelation();
                    relation.setRoleId(role.getId());
                    relation.setResourceId(common.getId());
                    return relation;
                }).collect(Collectors.toList());
        roleResourceRelationMapper.insertList(relations);
        resourceSize += 1;
        roleIds = roles.stream().map(Base::getId).collect(Collectors.toList());
        List<Resource> result3 = resourceService.getResourceListByRoleIds(roleIds);
        assertEquals(resourceSize, result3.size());
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
        ResourceCategory resourceCategory = new ResourceCategory();
        resourceCategory.setName("for test");
        resourceCategoryMapper.insert(resourceCategory);
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
        Resource resource1 = new Resource();
        resource1.setName("for test");
        resourceMapper.insert(resource1);
        ResourceCategory resourceCategory = new ResourceCategory();
        resourceCategory.setName("for test");
        resourceCategoryMapper.insert(resourceCategory);
        resource.setName("update name");
        id = resource1.getId();
        resource.setUrl("update url");
        resource.setDescription("update description");
        resource.setCategoryId(resourceCategory.getId());
        Resource oldResource = resourceService.getById(id);
        resourceService.updateResource(id, resource);
        Resource newResource = resourceService.getById(id);
        assertNotEquals(oldResource, newResource);
        assertEquals(resource.getName(), newResource.getName());
        assertEquals(resource.getUrl(), newResource.getUrl());
        assertEquals(resource.getDescription(), newResource.getDescription());
        assertEquals(resource.getCategoryId(), newResource.getCategoryId());
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

    @Test
    void list() {
        // not exist
        CommonPage<Resource> result = resourceService.list(-1L, "", "", 1, 20);
        assertEquals(0, result.getData().size());

        // exist
        ResourceCategory resourceCategory = new ResourceCategory();
        resourceCategory.setName("for test");
        resourceCategoryMapper.insert(resourceCategory);
        for (int i = 0; i < 10; i++) {
            Resource resource = new Resource();
            resource.setCategoryId(resourceCategory.getId());
            resource.setName("for test: " + i);
            resource.setUrl("/for/test/" + i);
            resourceMapper.insert(resource);
        }
        CommonPage<Resource> result1 = resourceService.list(resourceCategory.getId(), "", "", 1, 5);
        assertEquals(5, result1.getData().size());
        CommonPage<Resource> result2 = resourceService.list(null, null, null, 1, 5);
        assertEquals(5, result2.getData().size());
        CommonPage<Resource> result3 = resourceService.list(null, "test", null, 1, 5);
        assertEquals(5, result3.getData().size());
        CommonPage<Resource> result4 = resourceService.list(null, null, "/test", 1, 5);
        assertEquals(5, result4.getData().size());
    }
}