package online.ahayujie.mall.admin.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.service.MqService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
@Transactional
class MqServiceImplTest {
    @Autowired
    private MqService mqService;
    @Test
    void generateCorrelationData() {
        CorrelationData correlationData = mqService.generateCorrelationData("exchange", "key", "message");
        log.info("correlationData: " + correlationData);
        Assertions.assertNotNull(correlationData);
    }
}