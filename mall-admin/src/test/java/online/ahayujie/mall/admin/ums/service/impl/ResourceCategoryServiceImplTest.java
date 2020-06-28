package online.ahayujie.mall.admin.ums.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.ums.bean.dto.CreateResourceCategoryParam;
import online.ahayujie.mall.admin.ums.bean.dto.UpdateResourceCategoryParam;
import online.ahayujie.mall.admin.ums.bean.model.ResourceCategory;
import online.ahayujie.mall.admin.ums.exception.IllegalResourceCategoryException;
import online.ahayujie.mall.admin.ums.service.ResourceCategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class ResourceCategoryServiceImplTest {
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
        id = 1L;
        oldResourceCategories = resourceCategoryService.listAll();
        resourceCategoryService.delete(id);
        newResourceCategories = resourceCategoryService.listAll();
        assertEquals(oldResourceCategories.size() - 1, newResourceCategories.size());
    }
}