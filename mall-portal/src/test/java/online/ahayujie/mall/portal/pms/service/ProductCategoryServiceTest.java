package online.ahayujie.mall.portal.pms.service;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.portal.TestBase;
import online.ahayujie.mall.portal.pms.bean.dto.ProductCategoryDTO;
import online.ahayujie.mall.portal.pms.bean.model.ProductCategory;
import online.ahayujie.mall.portal.pms.mapper.ProductCategoryMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class ProductCategoryServiceTest extends TestBase {
    @Autowired
    private ProductCategoryMapper productCategoryMapper;
    @Autowired
    private ProductCategoryService productCategoryService;

    @Test
    void getFirstLevel() {
        for (int i = 0; i < 10; i++) {
            ProductCategory productCategory = new ProductCategory();
            productCategory.setParentId(ProductCategory.NON_PARENT_ID);
            productCategory.setName(getRandomString(5 + i));
            productCategory.setLevel(ProductCategory.FIRST_LEVEL);
            productCategory.setIsShow(ProductCategory.ShowStatus.SHOW.getValue());
            productCategory.setSort(i);
            productCategoryMapper.insert(productCategory);
        }
        List<ProductCategoryDTO> productCategoryDTOS = productCategoryService.getFirstLevel();
        List<ProductCategory> productCategories = productCategoryDTOS.stream().map(dto -> productCategoryMapper.selectById(dto.getId())).collect(Collectors.toList());
        Integer sort = Integer.MAX_VALUE;
        for (ProductCategory productCategory : productCategories) {
            assertTrue(sort >= productCategory.getSort());
            assertEquals(ProductCategory.ShowStatus.SHOW.getValue(), productCategory.getIsShow());
            sort = productCategory.getSort();
        }
    }

    @Test
    void getSecondLevel() {
        Long parentId = 1L;
        for (int i = 0; i < 10; i++) {
            ProductCategory productCategory = new ProductCategory();
            productCategory.setParentId(parentId);
            productCategory.setName(getRandomString(5 + i));
            productCategory.setLevel(ProductCategory.SECOND_LEVEL);
            productCategory.setIsShow(ProductCategory.ShowStatus.SHOW.getValue());
            productCategory.setSort(i);
            productCategoryMapper.insert(productCategory);
        }
        CommonPage<ProductCategoryDTO> page = productCategoryService.getSecondLevel(1L, 10L, parentId);
        assertEquals(10, page.getData().size());
        List<ProductCategory> productCategories = page.getData().stream().map(dto -> productCategoryMapper.selectById(dto.getId())).collect(Collectors.toList());
        Integer sort = Integer.MAX_VALUE;
        for (ProductCategory productCategory : productCategories) {
            assertEquals(parentId, productCategory.getParentId());
            assertTrue(sort >= productCategory.getSort());
            assertEquals(ProductCategory.ShowStatus.SHOW.getValue(), productCategory.getIsShow());
            sort = productCategory.getSort();
        }
    }
}