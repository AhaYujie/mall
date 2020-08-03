package online.ahayujie.mall.admin.pms.publisher.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.config.RabbitmqConfig;
import online.ahayujie.mall.admin.pms.bean.dto.DeleteProductCategoryMessageDTO;
import online.ahayujie.mall.admin.pms.bean.dto.UpdateProductCategoryMessageDTO;
import online.ahayujie.mall.admin.pms.publisher.ProductCategoryPublisher;
import online.ahayujie.mall.admin.service.MqService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author aha
 * @since 2020/8/3
 */
@Slf4j
@Service
public class ProductCategoryPublisherImpl implements ProductCategoryPublisher {
    private MqService mqService;

    private final ObjectMapper objectMapper;
    private final RabbitTemplate rabbitTemplate;

    public ProductCategoryPublisherImpl(ObjectMapper objectMapper, RabbitTemplate rabbitTemplate) {
        this.objectMapper = objectMapper;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Async
    @Override
    public void publishUpdateMsg(Long id) {
        try {
            UpdateProductCategoryMessageDTO messageDTO = new UpdateProductCategoryMessageDTO(id);
            String message = objectMapper.writeValueAsString(messageDTO);
            String exchange = RabbitmqConfig.PRODUCT_CATEGORY_UPDATE_EXCHANGE;
            CorrelationData correlationData = mqService.generateCorrelationData(exchange, "", message);
            rabbitTemplate.convertAndSend(exchange, "", message, correlationData);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }

    @Async
    @Override
    public void publishDeleteMsg(DeleteProductCategoryMessageDTO messageDTO) {
        try {
            String message = objectMapper.writeValueAsString(messageDTO);
            String exchange = RabbitmqConfig.PRODUCT_CATEGORY_DELETE_EXCHANGE;
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
