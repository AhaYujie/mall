package online.ahayujie.mall.admin.pms.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.pms.bean.dto.CreateProductCategoryParam;
import online.ahayujie.mall.admin.pms.bean.dto.UpdateProductCategoryParam;
import online.ahayujie.mall.admin.pms.bean.model.ProductCategory;
import online.ahayujie.mall.admin.pms.exception.IllegalProductCategoryException;
import online.ahayujie.mall.admin.pms.mapper.ProductCategoryMapper;
import online.ahayujie.mall.admin.pms.service.ProductCategoryService;
import online.ahayujie.mall.common.bean.model.Base;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class ProductCategoryServiceImplTest {
    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Test
    void create() {
        // illegal
        CreateProductCategoryParam param = new CreateProductCategoryParam();
        param.setParentId(-1L);
        Throwable throwable1 = assertThrows(IllegalProductCategoryException.class, () -> productCategoryService.create(param));
        log.debug(throwable1.getMessage());
        CreateProductCategoryParam param1 = new CreateProductCategoryParam();
        param1.setIsNav(-1);
        Throwable throwable2 = assertThrows(IllegalProductCategoryException.class, () -> productCategoryService.create(param1));
        log.debug(throwable2.getMessage());
        CreateProductCategoryParam param2 = new CreateProductCategoryParam();
        param2.setIsShow(-1);
        Throwable throwable3 = assertThrows(IllegalProductCategoryException.class, () -> productCategoryService.create(param2));
        log.debug(throwable3.getMessage());

        // legal
        List<CreateProductCategoryParam> params = new ArrayList<>();
        Random random = new Random();
        int size = random.nextInt(20);
        for (int i = 0; i < size; i++) {
            CreateProductCategoryParam param3 = new CreateProductCategoryParam();
            param3.setName("for test: " + i);
            param3.setIsShow(ProductCategory.ShowStatus.SHOW.getValue());
            param3.setIsNav(ProductCategory.NavStatus.SHOW.getValue());
            param3.setParentId(0L);
            param3.setDescription("test");
            param3.setIcon("icon");
            param3.setKeywords("keywords");
            param3.setProductUnit("unit");
            param3.setSort(0);
            params.add(param3);
        }
        List<ProductCategory> oldProductCategories = productCategoryMapper.selectAll();
        params.forEach(productCategoryService::create);
        List<ProductCategory> newProductCategories = productCategoryMapper.selectAll();
        log.debug("oldProductCategories: " + oldProductCategories);
        log.debug("newProductCategories: " + newProductCategories);
        assertEquals(oldProductCategories.size() + size, newProductCategories.size());
    }

    @Test
    void update() {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setName("for test");
        productCategoryMapper.insert(productCategory);

        // illegal
        Long id = -1L;
        UpdateProductCategoryParam param1 = new UpdateProductCategoryParam();
        Throwable throwable1 = assertThrows(IllegalProductCategoryException.class, () -> productCategoryService.update(id, param1));
        log.debug(throwable1.getMessage());
        UpdateProductCategoryParam param2 = new UpdateProductCategoryParam();
        param2.setParentId(-1L);
        Throwable throwable2 = assertThrows(IllegalProductCategoryException.class, () -> productCategoryService.update(productCategory.getId(), param2));
        log.debug(throwable2.getMessage());
        UpdateProductCategoryParam param3 = new UpdateProductCategoryParam();
        param3.setIsNav(-1);
        Throwable throwable3 = assertThrows(IllegalProductCategoryException.class, () -> productCategoryService.update(productCategory.getId(), param3));
        log.debug(throwable3.getMessage());
        UpdateProductCategoryParam param4 = new UpdateProductCategoryParam();
        param4.setIsShow(-1);
        Throwable throwable4 = assertThrows(IllegalProductCategoryException.class, () -> productCategoryService.update(productCategory.getId(), param4));
        log.debug(throwable4.getMessage());

        // legal
        UpdateProductCategoryParam param5 = new UpdateProductCategoryParam();
        param5.setName("update name");
        param5.setIsShow(ProductCategory.ShowStatus.SHOW.getValue());
        param5.setIsNav(ProductCategory.NavStatus.SHOW.getValue());
        param5.setParentId(ProductCategory.NON_PARENT_ID);
        param5.setDescription("update description");
        param5.setIcon("update icon");
        param5.setKeywords("update keywords");
        param5.setProductUnit("update unit");
        param5.setSort(0);
        productCategoryService.update(productCategory.getId(), param5);
        ProductCategory newProductCategory = productCategoryMapper.selectById(productCategory.getId());
        assertEquals(param5.getName(), newProductCategory.getName());
        assertEquals(param5.getIsNav(), newProductCategory.getIsNav());
        assertEquals(param5.getIsShow(), newProductCategory.getIsShow());
        assertEquals(param5.getDescription(), newProductCategory.getDescription());
        assertEquals(param5.getIcon(), newProductCategory.getIcon());
        assertEquals(param5.getKeywords(), newProductCategory.getKeywords());
        assertEquals(param5.getParentId(), newProductCategory.getParentId());
        assertEquals(param5.getProductUnit(), newProductCategory.getProductUnit());
        assertEquals(param5.getSort(), newProductCategory.getSort());
    }

    @Test
    void delete() {
        int size = new Random().nextInt(20);
        List<ProductCategory> productCategories = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ProductCategory productCategory = new ProductCategory();
            productCategory.setName("for test: " + i);
            productCategories.add(productCategory);
        }
        productCategories.forEach(productCategoryMapper::insert);
        List<ProductCategory> oldProductCategories = productCategoryMapper.selectAll();
        List<Long> ids = productCategories.stream().map(Base::getId).collect(Collectors.toList());
        ids.forEach(productCategoryService::delete);
        List<ProductCategory> newProductCategories = productCategoryMapper.selectAll();
        assertEquals(oldProductCategories.size() - size, newProductCategories.size());
    }

    @Test
    void updateNavStatus() {
        int size = new Random().nextInt(20) + 1;
        List<ProductCategory> productCategories = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ProductCategory productCategory = new ProductCategory();
            productCategory.setName("for test: " + i);
            productCategories.add(productCategory);
        }
        productCategories.forEach(productCategoryMapper::insert);
        List<Long> ids = productCategories.stream().map(Base::getId).collect(Collectors.toList());

        // illegal isNav
        Integer isNav = -1;
        Integer finalIsNav = isNav;
        Throwable throwable = assertThrows(IllegalProductCategoryException.class, () -> productCategoryService.updateNavStatus(ids, finalIsNav));
        log.debug(throwable.getMessage());

        // legal
        isNav = ProductCategory.NavStatus.SHOW.getValue();
        productCategoryService.updateNavStatus(ids, isNav);
        List<ProductCategory> productCategories1 = ids.stream().map(productCategoryMapper::selectById).collect(Collectors.toList());
        for (ProductCategory productCategory : productCategories1) {
            assertEquals(isNav, productCategory.getIsNav());
        }
    }

    @Test
    void updateShowStatus() {
        int size = new Random().nextInt(20) + 1;
        List<ProductCategory> productCategories = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ProductCategory productCategory = new ProductCategory();
            productCategory.setName("for test: " + i);
            productCategories.add(productCategory);
        }
        productCategories.forEach(productCategoryMapper::insert);
        List<Long> ids = productCategories.stream().map(Base::getId).collect(Collectors.toList());

        // illegal isNav
        Integer isShow = -1;
        Integer finalIsNav = isShow;
        Throwable throwable = assertThrows(IllegalProductCategoryException.class, () -> productCategoryService.updateShowStatus(ids, finalIsNav));
        log.debug(throwable.getMessage());

        // legal
        isShow = ProductCategory.ShowStatus.SHOW.getValue();
        productCategoryService.updateShowStatus(ids, isShow);
        List<ProductCategory> productCategories1 = ids.stream().map(productCategoryMapper::selectById).collect(Collectors.toList());
        for (ProductCategory productCategory : productCategories1) {
            assertEquals(isShow, productCategory.getIsShow());
        }
    }
}