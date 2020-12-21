package online.ahayujie.mall.portal.oms.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.portal.config.RabbitmqConfig;
import online.ahayujie.mall.portal.oms.bean.dto.OrderCancelMsgDTO;
import online.ahayujie.mall.portal.oms.service.OrderSettingService;
import online.ahayujie.mall.portal.service.MqService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author aha
 * @since 2020/12/18
 */
@Slf4j
@Service
public class OrderPublisherImpl implements OrderPublisher {
    private MqService mqService;
    private OrderSettingService orderSettingService;

    private final ObjectMapper objectMapper;
    private final RabbitTemplate rabbitTemplate;

    public OrderPublisherImpl(ObjectMapper objectMapper, RabbitTemplate rabbitTemplate) {
        this.objectMapper = objectMapper;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Async
    @Override
    public void publishOrderTimeoutCancelDelayedMsg(OrderCancelMsgDTO orderCancelMsgDTO) {
        try {
            String message = objectMapper.writeValueAsString(orderCancelMsgDTO);
            String exchange = RabbitmqConfig.ORDER_TTL_EXCHANGE;
            String routingKey = RabbitmqConfig.ORDER_CANCEL_TTL_ROUTING_KEY;
            CorrelationData correlationData = mqService.generateCorrelationData(exchange, routingKey, message);
            rabbitTemplate.convertAndSend(exchange, routingKey, message, msg -> {
                int millisecond = orderSettingService.getUnPayTimeout() * 60 * 1000;
                msg.getMessageProperties().setExpiration(Integer.toString(millisecond));
                return msg;
            }, correlationData);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @Autowired
    public void setMqService(MqService mqService) {
        this.mqService = mqService;
    }

    @Autowired
    public void setOrderSettingService(OrderSettingService orderSettingService) {
        this.orderSettingService = orderSettingService;
    }
}
