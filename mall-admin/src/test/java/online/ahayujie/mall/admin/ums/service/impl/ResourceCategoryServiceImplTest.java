package online.ahayujie.mall.admin.ums.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.ums.bean.dto.CreateResourceCategoryParam;
import online.ahayujie.mall.admin.ums.bean.dto.CreateResourceParam;
import online.ahayujie.mall.admin.ums.bean.dto.UpdateResourceCategoryParam;
import online.ahayujie.mall.admin.ums.bean.model.Resource;
import online.ahayujie.mall.admin.ums.bean.model.ResourceCategory;
import online.ahayujie.mall.admin.ums.exception.IllegalResourceCategoryException;
import online.ahayujie.mall.admin.ums.mapper.ResourceCategoryMapper;
import online.ahayujie.mall.admin.ums.service.ResourceCategoryService;
import online.ahayujie.mall.admin.ums.service.ResourceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class ResourceCategoryServiceImplTest {
    @Autowired
    private ResourceCategoryMapper resourceCategoryMapper;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ResourceCategoryService resourceCategoryService;

    @Test
    void listAll() {
        List<ResourceCategory> resourceCategories = resourceCategoryService.listAll();
        assertDoesNotThrow(() -> {
            int sort = resourceCategories.get(0).getSort();
            for (ResourceCategory resourceCategory : resourceCategories) {
                if (sort < resourceCategory.getSort()) {
                    throw new IllegalArgumentException();
                }
            }
        });
        log.debug("resourceCategories: " + resourceCategories);
    }

    @Test
    void create() {
        CreateResourceCategoryParam resourceCategory = new CreateResourceCategoryParam();
        resourceCategory.setName("test");
        resourceCategory.setSort(1);
        List<ResourceCategory> oldResourceCategories = resourceCategoryService.listAll();
        resourceCategoryService.create(resourceCategory);
        List<ResourceCategory> newResourceCategories = resourceCategoryService.listAll();
        assertEquals(oldResourceCategories.size() + 1, newResourceCategories.size());
        log.debug("oldResourceCategories: " + oldResourceCategories);
        log.debug("newResourceCategories: " + newResourceCategories);
    }

    @Test
    void update() {
        Long id;
        UpdateResourceCategoryParam param;

        // illegal resource category
        id = null;
        param = new UpdateResourceCategoryParam();
        Long finalId = id;
        UpdateResourceCategoryParam finalParam = param;
        Throwable throwable1 = assertThrows(IllegalResourceCategoryException.class, () -> resourceCategoryService.update(finalId, finalParam));
        log.debug(throwable1.getMessage());

        // legal
        id = 1L;
        param = new UpdateResourceCategoryParam();
        param.setName("update name");
        param.setSort(100);
        ResourceCategory oldResourceCategory = resourceCategoryService.getById(id);
        resourceCategoryService.update(id, param);
        ResourceCategory newResourceCategory = resourceCategoryService.getById(id);
        assertNotEquals(oldResourceCategory, newResourceCategory);
        log.debug("oldResourceCategory: " + oldResourceCategory);
        log.debug("newResourceCategory: " + newResourceCategory);
    }

    @Test
    void delete() {
        Long id;

        // id null
        id = null;
        List<ResourceCategory> oldResourceCategories = resourceCategoryService.listAll();
        resourceCategoryService.delete(id);
        List<ResourceCategory> newResourceCategories = resourceCategoryService.listAll();
        assertEquals(oldResourceCategories.size(), newResourceCategories.size());

        // id not null
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
        List<Resource> newResources = resourceService.getByCategoryId(resourceCategory.getId());
        assertEquals(size, newResources.size());
        resourceCategoryService.delete(resourceCategory.getId());
        List<Resource> oldResources = resourceService.getByCategoryId(resourceCategory.getId());
        assertEquals(0, oldResources.size());
    }
}