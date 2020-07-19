package online.ahayujie.mall.admin.pms.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.pms.bean.model.ProductSpecification;
import online.ahayujie.mall.admin.pms.bean.model.ProductSpecificationValue;
import online.ahayujie.mall.admin.pms.exception.IllegalProductSpecificationException;
import online.ahayujie.mall.admin.pms.mapper.ProductSpecificationMapper;
import online.ahayujie.mall.admin.pms.mapper.ProductSpecificationValueMapper;
import online.ahayujie.mall.admin.pms.service.ProductSpecificationService;
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
class ProductSpecificationServiceImplTest {
    @Autowired
    private ProductSpecificationService productSpecificationService;
    @Autowired
    private ProductSpecificationMapper productSpecificationMapper;
    @Autowired
    private ProductSpecificationValueMapper productSpecificationValueMapper;

    @Test
    void validate() {
        // illegal
        ProductSpecificationValue specificationValue = new ProductSpecificationValue();
        specificationValue.setType(-1);
        Throwable throwable = assertThrows(IllegalProductSpecificationException.class, () -> productSpecificationService.validate(specificationValue));
        log.debug(throwable.getMessage());
        ProductSpecificationValue specificationValue1 = new ProductSpecificationValue();
        specificationValue1.setType(ProductSpecificationValue.Type.TEXT.getValue());
        specificationValue1.setValue("{}");
        Throwable throwable1 = assertThrows(IllegalProductSpecificationException.class, () -> productSpecificationService.validate(specificationValue1));
        log.debug(throwable1.getMessage());
        ProductSpecificationValue specificationValue2 = new ProductSpecificationValue();
        specificationValue2.setType(ProductSpecificationValue.Type.IMAGE_TEXT.getValue());
        specificationValue2.setValue("{}");
        Throwable throwable2 = assertThrows(IllegalProductSpecificationException.class, () -> productSpecificationService.validate(specificationValue2));
        log.debug(throwable2.getMessage());

        // legal
        ProductSpecificationValue specificationValue3 = new ProductSpecificationValue();
        specificationValue3.setType(ProductSpecificationValue.Type.TEXT.getValue());
        specificationValue3.setValue("{\"name\" : \"test\"}");
        productSpecificationService.validate(specificationValue3);
        ProductSpecificationValue specificationValue4 = new ProductSpecificationValue();
        specificationValue4.setType(ProductSpecificationValue.Type.IMAGE_TEXT.getValue());
        specificationValue4.setValue("{\"name\" : \"test\", \"image\" : \"test\"}");
        productSpecificationService.validate(specificationValue4);
    }

    @Test
    void saveSpecifications() {
        List<ProductSpecification> specifications = new ArrayList<>();
        for (int i = 0; i < new Random().nextInt(20) + 1; i++) {
            ProductSpecification specification = new ProductSpecification();
            specification.setName("for test: " + i);
            specifications.add(specification);
        }
        List<ProductSpecification> oldSpecifications = productSpecificationMapper.selectAll();
        productSpecificationService.saveSpecifications(specifications);
        List<ProductSpecification> newSpecifications = productSpecificationMapper.selectAll();
        assertEquals(oldSpecifications.size() + specifications.size(), newSpecifications.size());
        log.debug("oldSpecifications: " + oldSpecifications);
        log.debug("newSpecifications: " + newSpecifications);
    }

    @Test
    void saveSpecificationValues() {
        List<ProductSpecificationValue> specificationValues = new ArrayList<>();
        for (int i = 0; i < new Random().nextInt(20) + 1; i++) {
            ProductSpecificationValue specificationValue = new ProductSpecificationValue();
            specificationValue.setValue("for test: " + i);
            specificationValues.add(specificationValue);
        }
        List<ProductSpecificationValue> oldSpecificationValues = productSpecificationValueMapper.selectAll();
        productSpecificationService.saveSpecificationValues(specificationValues);
        List<ProductSpecificationValue> newSpecificationValues = productSpecificationValueMapper.selectAll();
        assertEquals(oldSpecificationValues.size() + specificationValues.size(), newSpecificationValues.size());
        log.debug("oldSpecificationValues: " + oldSpecificationValues);
        log.debug("newSpecificationValues: " + newSpecificationValues);
    }
}