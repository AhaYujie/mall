package online.ahayujie.mall.admin.pms.publisher.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.config.RabbitmqConfig;
import online.ahayujie.mall.admin.pms.bean.dto.UpdateProductMessageDTO;
import online.ahayujie.mall.admin.pms.publisher.ProductPublisher;
import online.ahayujie.mall.admin.service.MqService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author aha
 * @since 2020/8/2
 */
@Slf4j
@Service
public class ProductPublisherImpl implements ProductPublisher {
    private MqService mqService;

    private final ObjectMapper objectMapper;
    private final RabbitTemplate rabbitTemplate;

    public ProductPublisherImpl(ObjectMapper objectMapper, RabbitTemplate rabbitTemplate) {
        this.objectMapper = objectMapper;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Async
    @Override
    public void publishUpdateMsg(Long id) {
        try {
            UpdateProductMessageDTO messageDTO = new UpdateProductMessageDTO(id);
            String message = objectMapper.writeValueAsString(messageDTO);
            String exchange = RabbitmqConfig.PRODUCT_UPDATE_EXCHANGE;
            CorrelationData correlationData = mqService.generateCorrelationData(exchange, "", message);
            rabbitTemplate.convertAndSend(exchange, "", message, correlationData);
        } catch (JsonProcessingException e) {
            log.warn(e.getMessage());
            log.warn(Arrays.toString(e.getStackTrace()));
        }
    }

    @Autowired
    public void setMqService(MqService mqService) {
        this.mqService = mqService;
    }
}
