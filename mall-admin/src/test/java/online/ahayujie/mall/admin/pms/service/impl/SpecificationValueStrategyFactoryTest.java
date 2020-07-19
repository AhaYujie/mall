package online.ahayujie.mall.admin.pms.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.pms.bean.model.ProductSpecificationValue;
import online.ahayujie.mall.admin.pms.service.SpecificationValueStrategy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class SpecificationValueStrategyFactoryTest {
    @Autowired
    private SpecificationValueStrategyFactory factory;

    @Test
    void getStrategy() {
        // exist
        SpecificationValueStrategy strategy = factory.getStrategy(ProductSpecificationValue.Type.TEXT.getName());
        assertNotNull(strategy);
        SpecificationValueStrategy strategy1 = factory.getStrategy(ProductSpecificationValue.Type.IMAGE_TEXT.getName());
        assertNotNull(strategy1);

        // not exist
        SpecificationValueStrategy strategy2 = factory.getStrategy("not exist strategy");
        assertNull(strategy2);
    }

    @Test
    void testGetStrategy() {
        // exist
        SpecificationValueStrategy strategy = factory.getStrategy(ProductSpecificationValue.Type.TEXT.getValue());
        assertNotNull(strategy);
        SpecificationValueStrategy strategy1 = factory.getStrategy(ProductSpecificationValue.Type.IMAGE_TEXT.getValue());
        assertNotNull(strategy1);

        // not exist
        SpecificationValueStrategy strategy2 = factory.getStrategy(-1);
        assertNull(strategy2);
    }
}