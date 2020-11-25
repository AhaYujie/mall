package online.ahayujie.mall.admin.pms.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.pms.bean.dto.CreateProductCategoryParam;
import online.ahayujie.mall.admin.pms.bean.dto.ProductCategoryTree;
import online.ahayujie.mall.admin.pms.bean.dto.UpdateProductCategoryParam;
import online.ahayujie.mall.admin.pms.bean.model.ProductCategory;
import online.ahayujie.mall.admin.pms.exception.IllegalProductCategoryException;
import online.ahayujie.mall.admin.pms.mapper.ProductCategoryMapper;
import online.ahayujie.mall.admin.pms.service.ProductCategoryService;
import online.ahayujie.mall.common.api.CommonPage;
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
        productCategoryMapper.deleteById(1L);
        CreateProductCategoryParam param2 = new CreateProductCategoryParam();
        param2.setParentId(1L);
        assertThrows(IllegalProductCategoryException.class, () -> productCategoryService.create(param2));

        // legal
        CreateProductCategoryParam parent = new CreateProductCategoryParam();
        parent.setName("parent");
        parent.setIsNav(ProductCategory.NavStatus.SHOW.getValue());
        parent.setParentId(ProductCategory.NON_PARENT_ID);
        parent.setDescription("test");
        parent.setIcon("icon");
        parent.setSort(0);
        List<ProductCategory> oldList = productCategoryMapper.selectAll();
        productCategoryService.create(parent);
        List<ProductCategory> newList = productCategoryMapper.selectAll();
        List<ProductCategory> result = getNewFromOld(oldList, newList);
        assertEquals(1, result.size());
        ProductCategory parentCategory = result.get(0);
        assertEquals(ProductCategory.NON_PARENT_ID, parentCategory.getParentId());
        CreateProductCategoryParam sub = new CreateProductCategoryParam();
        sub.setName("sub");
        sub.setIsNav(ProductCategory.NavStatus.NOT_SHOW.getValue());
        sub.setParentId(parentCategory.getId());
        sub.setDescription("test");
        sub.setIcon("icon");
        sub.setSort(0);
        oldList = productCategoryMapper.selectAll();
        productCategoryService.create(sub);
        newList = productCategoryMapper.selectAll();
        result = getNewFromOld(oldList, newList);
        assertEquals(1, result.size());
        ProductCategory subCategory = result.get(0);
        assertEquals(parentCategory.getId(), subCategory.getParentId());
        CreateProductCategoryParam subSub = new CreateProductCategoryParam();
        subSub.setName("subSub");
        subSub.setIsNav(ProductCategory.NavStatus.SHOW.getValue());
        subSub.setParentId(subCategory.getId());
        subSub.setDescription("test");
        subSub.setIcon("icon");
        subSub.setSort(0);
        oldList = productCategoryMapper.selectAll();
        productCategoryService.create(subSub);
        newList = productCategoryMapper.selectAll();
        result = getNewFromOld(oldList, newList);
        assertEquals(1, result.size());
        ProductCategory subSubCategory = result.get(0);
        assertEquals(subCategory.getId(), subSubCategory.getParentId());
    }

    private List<ProductCategory> getNewFromOld(List<ProductCategory> oldList, List<ProductCategory> newList) {
        List<ProductCategory> productCategories = new ArrayList<>();
        for (ProductCategory productCategory : newList) {
            if (!oldList.contains(productCategory)) {
                productCategories.add(productCategory);
            }
        }
        return productCategories;
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
        productCategoryMapper.deleteById(productCategory.getId() + 1);
        UpdateProductCategoryParam param = new UpdateProductCategoryParam();
        param.setParentId(productCategory.getId() + 1);
        assertThrows(IllegalProductCategoryException.class, () -> productCategoryService.update(productCategory.getId(), param));
        param.setParentId(productCategory.getId());
        assertThrows(IllegalProductCategoryException.class, () -> productCategoryService.update(productCategory.getId(), param));

        // legal
        UpdateProductCategoryParam param5 = new UpdateProductCategoryParam();
        param5.setName("update name");
        param5.setIsNav(ProductCategory.NavStatus.SHOW.getValue());
        param5.setParentId(ProductCategory.NON_PARENT_ID);
        param5.setDescription("update description");
        param5.setIcon("update icon");
        param5.setSort(0);
        productCategoryService.update(productCategory.getId(), param5);
        ProductCategory newProductCategory = productCategoryMapper.selectById(productCategory.getId());
        assertEquals(param5.getName(), newProductCategory.getName());
        assertEquals(param5.getIsNav(), newProductCategory.getIsNav());
        assertEquals(param5.getDescription(), newProductCategory.getDescription());
        assertEquals(param5.getIcon(), newProductCategory.getIcon());
        assertEquals(param5.getParentId(), newProductCategory.getParentId());
        assertEquals(param5.getSort(), newProductCategory.getSort());
        ProductCategory productCategory1 = new ProductCategory();
        productCategory1.setName("sub");
        productCategory1.setParentId(ProductCategory.NON_PARENT_ID);
        productCategoryMapper.insert(productCategory1);
        UpdateProductCategoryParam param4 = new UpdateProductCategoryParam();
        param4.setParentId(productCategory.getId());
        productCategoryService.update(productCategory1.getId(), param4);
        productCategory1 = productCategoryMapper.selectById(productCategory1.getId());
        assertEquals(productCategory.getId(), productCategory1.getParentId());
        ProductCategory productCategory2 = new ProductCategory();
        productCategory2.setName("subSub");
        productCategory2.setParentId(ProductCategory.NON_PARENT_ID);
        productCategoryMapper.insert(productCategory2);
        UpdateProductCategoryParam param6 = new UpdateProductCategoryParam();
        param6.setParentId(productCategory1.getId());
        productCategoryService.update(productCategory2.getId(), param6);
        productCategory2 = productCategoryMapper.selectById(productCategory2.getId());
        assertEquals(productCategory1.getId(), productCategory2.getParentId());
    }

    @Test
    void delete() {
        int size = new Random().nextInt(5);
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

        ProductCategory parent = new ProductCategory();
        parent.setName("parent");
        parent.setParentId(ProductCategory.NON_PARENT_ID);
        productCategoryMapper.insert(parent);
        ProductCategory child1 = new ProductCategory();
        child1.setName("child1");
        child1.setParentId(parent.getId());
        productCategoryMapper.insert(child1);
        ProductCategory child2 = new ProductCategory();
        child2.setName("child2");
        child2.setParentId(parent.getId());
        productCategoryMapper.insert(child2);
        ProductCategory subChild = new ProductCategory();
        subChild.setName("subChild");
        subChild.setParentId(child1.getId());
        productCategoryMapper.insert(subChild);
        productCategoryService.delete(parent.getId());
        assertNull(productCategoryMapper.selectById(parent.getId()));
        assertNull(productCategoryMapper.selectById(child1.getId()));
        assertNull(productCategoryMapper.selectById(child2.getId()));
        assertNull(productCategoryMapper.selectById(subChild.getId()));
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
    void getPageByParentId() {
        ProductCategory parent = new ProductCategory();
        parent.setName("parent");
        parent.setParentId(ProductCategory.NON_PARENT_ID);
        productCategoryMapper.insert(parent);
        ProductCategory child1 = new ProductCategory();
        child1.setName("child1");
        child1.setParentId(parent.getId());
        productCategoryMapper.insert(child1);
        child1 = productCategoryMapper.selectById(child1.getId());
        ProductCategory child2 = new ProductCategory();
        child2.setName("child2");
        child2.setParentId(parent.getId());
        productCategoryMapper.insert(child2);
        child2 = productCategoryMapper.selectById(child2.getId());
        ProductCategory subChild = new ProductCategory();
        subChild.setName("subChild");
        subChild.setParentId(child1.getId());
        productCategoryMapper.insert(subChild);
        subChild = productCategoryMapper.selectById(subChild.getId());

        CommonPage<ProductCategory> result = productCategoryService.getPageByParentId(parent.getId(), 1, 5);
        assertEquals(2, result.getData().size());
        assertTrue(result.getData().contains(child1));
        assertTrue(result.getData().contains(child2));
        assertFalse(result.getData().contains(subChild));
    }

    @Test
    void listWithChildren() {
        ProductCategory parent = new ProductCategory();
        parent.setName("parent");
        parent.setParentId(ProductCategory.NON_PARENT_ID);
        productCategoryMapper.insert(parent);
        ProductCategory child1 = new ProductCategory();
        child1.setName("child1");
        child1.setParentId(parent.getId());
        productCategoryMapper.insert(child1);
        ProductCategory child2 = new ProductCategory();
        child2.setName("child2");
        child2.setParentId(parent.getId());
        productCategoryMapper.insert(child2);
        ProductCategory subChild = new ProductCategory();
        subChild.setName("subChild");
        subChild.setParentId(child1.getId());
        productCategoryMapper.insert(subChild);
        ProductCategory subChild1 = new ProductCategory();
        subChild1.setName("subChild1");
        subChild1.setParentId(child2.getId());
        productCategoryMapper.insert(subChild1);
        ProductCategory subSub = new ProductCategory();
        subSub.setName("subSub");
        subSub.setParentId(subChild1.getId());
        productCategoryMapper.insert(subSub);

        List<ProductCategoryTree> trees = productCategoryService.listWithChildren(parent.getId());
        assertEquals(2, trees.size());
        ProductCategoryTree tree1 = null, tree2 = null;
        for (ProductCategoryTree tree : trees) {
            if (tree.getProductCategory().getId().equals(child1.getId())) {
                tree1 = tree;
            } else if (tree.getProductCategory().getId().equals(child2.getId())) {
                tree2 = tree;
            }
        }
        assertNotNull(tree1);
        assertNotNull(tree2);
        assertEquals(1, tree1.getChildren().size());
        ProductCategoryTree subChildTree = tree1.getChildren().get(0);
        assertEquals(subChild.getId(), subChildTree.getProductCategory().getId());
        assertTrue(subChildTree.getChildren().isEmpty());
        ProductCategoryTree subChildTree1 = tree2.getChildren().get(0);
        assertEquals(subChild1.getId(), subChildTree1.getProductCategory().getId());
        assertEquals(1, subChildTree1.getChildren().size());
        assertEquals(subSub.getId(), subChildTree1.getChildren().get(0).getProductCategory().getId());
    }
}