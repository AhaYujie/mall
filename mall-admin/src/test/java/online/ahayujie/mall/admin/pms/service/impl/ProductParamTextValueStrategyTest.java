package online.ahayujie.mall.admin.pms.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.pms.exception.IllegalProductParamException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class ProductParamTextValueStrategyTest {
    @Autowired
    private ProductParamTextValueStrategy strategy;

    @Test
    void validate() {
        // illegal
        Throwable throwable = assertThrows(IllegalProductParamException.class, () -> strategy.validate("{}"));
        log.debug(throwable.getMessage());
        Throwable throwable1 = assertThrows(IllegalProductParamException.class, () -> strategy.validate("{\"test\" : \"test\"}"));
        log.debug(throwable1.getMessage());

        // legal
        strategy.validate("{\"name\" : \"test\"}");
    }
}