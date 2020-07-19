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
class SpecificationImageTextValueStrategyTest {
    @Autowired
    private SpecificationImageTextValueStrategy strategy;

    @Test
    void validate() {
        // illegal
        assertThrows(IllegalProductSpecificationException.class, () -> strategy.validate("{}"));
        assertThrows(IllegalProductSpecificationException.class, () -> strategy.validate("{\"test\" : \"test\"}"));
        assertThrows(IllegalProductSpecificationException.class, () -> strategy.validate("{\"name\" : \"test\"}"));
        assertThrows(IllegalProductSpecificationException.class, () -> strategy.validate("{\"image\" : \"test\"}"));

        // legal
        strategy.validate("{\"name\" : \"test\", \"image\" : \"test\"}");
    }

    @Test
    void getText() {
        // illegal
        assertThrows(IllegalProductSpecificationException.class, () -> strategy.getText("{}"));
        assertThrows(IllegalProductSpecificationException.class, () -> strategy.getText("{\"test\" : \"test\"}"));
        assertThrows(IllegalProductSpecificationException.class, () -> strategy.getText("{\"name\" : \"test\"}"));
        assertThrows(IllegalProductSpecificationException.class, () -> strategy.getText("{\"image\" : \"test\"}"));

        // legal
        String text = strategy.getText("{\"name\" : \"aha\", \"image\" : \"test\"}");
        assertEquals("aha", text);
    }
}