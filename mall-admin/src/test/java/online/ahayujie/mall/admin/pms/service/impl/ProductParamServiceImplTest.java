package online.ahayujie.mall.admin.pms.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.pms.bean.model.ProductParam;
import online.ahayujie.mall.admin.pms.exception.IllegalProductParamException;
import online.ahayujie.mall.admin.pms.mapper.ProductParamMapper;
import online.ahayujie.mall.admin.pms.service.ProductParamService;
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
class ProductParamServiceImplTest {
    @Autowired
    private ProductParamService productParamService;
    @Autowired
    private ProductParamMapper productParamMapper;

    @Test
    void validate() {
        // illegal
        ProductParam productParam = new ProductParam();
        productParam.setType(-1);
        Throwable throwable = assertThrows(IllegalProductParamException.class, () -> productParamService.validate(productParam));
        log.debug(throwable.getMessage());
        ProductParam productParam1 = new ProductParam();
        productParam1.setType(ProductParam.Type.TEXT.getValue());
        productParam1.setValue("{\"test\" : \"test\"}");
        Throwable throwable1 = assertThrows(IllegalProductParamException.class, () -> productParamService.validate(productParam1));
        log.debug(throwable1.getMessage());
        ProductParam productParam2 = new ProductParam();
        productParam2.setType(ProductParam.Type.IMAGE_TEXT.getValue());
        productParam2.setValue("{\"test\" : \"test\"}");
        Throwable throwable2 = assertThrows(IllegalProductParamException.class, () -> productParamService.validate(productParam2));
        log.debug(throwable2.getMessage());

        // legal
        ProductParam productParam3 = new ProductParam();
        productParam3.setType(ProductParam.Type.TEXT.getValue());
        productParam3.setValue("{\"name\" : \"test\"}");
        productParamService.validate(productParam3);
        ProductParam productParam4 = new ProductParam();
        productParam4.setType(ProductParam.Type.IMAGE_TEXT.getValue());
        productParam4.setValue("{\"name\" : \"test\", \"image\" : \"test\"}");
        productParamService.validate(productParam4);
    }

    @Test
    void save() {
        List<ProductParam> productParams = new ArrayList<>();
        int size = new Random().nextInt(20) + 1;
        for (int i = 0; i < size; i++) {
            ProductParam productParam = new ProductParam();
            productParam.setName("for test: " + i);
            productParams.add(productParam);
        }
        List<ProductParam> oldProductParams = productParamMapper.selectAll();
        productParamService.save(productParams);
        List<ProductParam> newProductParams = productParamMapper.selectAll();
        assertEquals(oldProductParams.size() + size, newProductParams.size());
        log.debug("oldProductParams: " + oldProductParams);
        log.debug("newProductParams: " + newProductParams);
    }
}