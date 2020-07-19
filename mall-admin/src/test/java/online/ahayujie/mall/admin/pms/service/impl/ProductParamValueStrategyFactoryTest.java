package online.ahayujie.mall.admin.pms.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.pms.bean.model.ProductParam;
import online.ahayujie.mall.admin.pms.service.ProductParamValueStrategy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class ProductParamValueStrategyFactoryTest {
    @Autowired
    private ProductParamValueStrategyFactory factory;

    @Test
    void getStrategy() {
        // not exist
        ProductParamValueStrategy strategy = factory.getStrategy("not exist strategy");
        assertNull(strategy);

        // exist
        ProductParamValueStrategy strategy1 = factory.getStrategy(ProductParam.Type.TEXT.getName());
        assertNotNull(strategy1);
        ProductParamValueStrategy strategy2 = factory.getStrategy(ProductParam.Type.IMAGE_TEXT.getName());
        assertNotNull(strategy2);
    }

    @Test
    void testGetStrategy() {
        // not exist
        ProductParamValueStrategy strategy = factory.getStrategy(-1);
        assertNull(strategy);

        // exist
        ProductParamValueStrategy strategy1 = factory.getStrategy(ProductParam.Type.TEXT.getValue());
        assertNotNull(strategy1);
        ProductParamValueStrategy strategy2 = factory.getStrategy(ProductParam.Type.IMAGE_TEXT.getValue());
        assertNotNull(strategy2);
    }
}