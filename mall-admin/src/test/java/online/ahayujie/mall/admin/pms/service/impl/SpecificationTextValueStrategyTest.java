package online.ahayujie.mall.admin.pms.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.pms.exception.IllegalProductSpecificationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class SpecificationTextValueStrategyTest {
    @Autowired
    private SpecificationTextValueStrategy strategy;

    @Test
    void validate() {
        // illegal
        assertThrows(IllegalProductSpecificationException.class, () -> strategy.validate("{\"test\" : \"test\"}"));
        assertThrows(IllegalProductSpecificationException.class, () -> strategy.validate(""));

        // legal
        strategy.validate("{\"name\" : \"test\"}");
    }

    @Test
    void getText() {
        // illegal
        assertThrows(IllegalProductSpecificationException.class, () -> strategy.getText("{\"test\" : \"test\"}"));
        assertThrows(IllegalProductSpecificationException.class, () -> strategy.getText(""));

        // legal
        String text = strategy.getText("{\"name\" : \"aha\"}");
        assertEquals("aha", text);
    }
}