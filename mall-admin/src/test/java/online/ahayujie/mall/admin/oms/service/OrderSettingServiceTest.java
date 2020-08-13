package online.ahayujie.mall.admin.oms.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class OrderSettingServiceTest {
    @Autowired
    private OrderSettingService orderSettingService;

    @Test
    void updateUnPayTimeOut() {
        // illegal
        Throwable throwable = assertThrows(IllegalArgumentException.class, () -> orderSettingService.updateUnPayTimeOut(-1));
        log.debug(throwable.getMessage());

        // legal
        Integer time = 20;
        orderSettingService.updateUnPayTimeOut(time);
        assertEquals(time, orderSettingService.getUnPayTimeout());
    }
}