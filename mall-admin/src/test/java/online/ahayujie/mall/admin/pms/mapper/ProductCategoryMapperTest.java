package online.ahayujie.mall.admin.pms.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.pms.bean.model.ProductCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class ProductCategoryMapperTest {
    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Test
    void selectAll() {
        List<ProductCategory> productCategories = new ArrayList<>();
        int size = new Random().nextInt(20);
        for (int i = 0; i < size; i++) {
            ProductCategory productCategory = new ProductCategory();
            productCategory.setName("for test: " + i);
            productCategories.add(productCategory);
        }
        List<ProductCategory> oldProductCategories = productCategoryMapper.selectAll();
        productCategories.forEach(productCategoryMapper::insert);
        List<ProductCategory> newProductCategories = productCategoryMapper.selectAll();
        log.debug("oldProductCategories: " + oldProductCategories);
        log.debug("newProductCategories: " + newProductCategories);
        assertEquals(oldProductCategories.size() + size, newProductCategories.size());
    }

    @Test
    void selectByParentId() {
        ProductCategory parent = new ProductCategory();
        parent.setName("parent");
        parent.setParentId(ProductCategory.NON_PARENT_ID);
        productCategoryMapper.insert(parent);
        List<ProductCategory> productCategories = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            ProductCategory productCategory = new ProductCategory();
            productCategory.setName("for test: " + i);
            productCategory.setParentId(parent.getId());
            productCategories.add(productCategory);
        }
        productCategories.forEach(productCategoryMapper::insert);
        Page<ProductCategory> page1 = new Page<>(1, 2);
        IPage<ProductCategory> result1 = productCategoryMapper.selectByParentId(page1, parent.getId());
        log.debug("result1: " + result1);
        assertEquals(page1.getSize(), result1.getRecords().size());
        Page<ProductCategory> page2 = new Page<>(2, 2);
        IPage<ProductCategory> result2 = productCategoryMapper.selectByParentId(page2, parent.getId());
        log.debug("result2: " + result2);
        assertEquals(page2.getSize(), result2.getRecords().size());
    }
}