package online.ahayujie.mall.admin.oms.publisher.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.config.RabbitmqConfig;
import online.ahayujie.mall.admin.oms.bean.dto.OrderCancelMsgDTO;
import online.ahayujie.mall.admin.oms.bean.dto.OrderCancelledMsgDTO;
import online.ahayujie.mall.admin.oms.bean.dto.OrderDeliverMsgDTO;
import online.ahayujie.mall.admin.oms.publisher.OrderPublisher;
import online.ahayujie.mall.admin.oms.service.OrderSettingService;
import online.ahayujie.mall.admin.service.MqService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author aha
 * @since 2020/8/16
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
            log.warn(e.toString());
            log.warn(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void publishOrderCancelledMsg(OrderCancelledMsgDTO orderCancelledMsgDTO) {
        try {
            String message = objectMapper.writeValueAsString(orderCancelledMsgDTO);
            String exchange = RabbitmqConfig.ORDER_CANCELLED_EXCHANGE;
            CorrelationData correlationData = mqService.generateCorrelationData(exchange, "", message);
            rabbitTemplate.convertAndSend(exchange, "", message, correlationData);
        } catch (JsonProcessingException e) {
            log.warn(e.toString());
            log.warn(Arrays.toString(e.getStackTrace()));
        }
    }

    @Async
    @Override
    public void publishOrderDeliverMsg(OrderDeliverMsgDTO orderDeliverMsgDTO) {
        try {
            String message = objectMapper.writeValueAsString(orderDeliverMsgDTO);
            String exchange = RabbitmqConfig.ORDER_DELIVER_EXCHANGE;
            CorrelationData correlationData = mqService.generateCorrelationData(exchange, "", message);
            rabbitTemplate.convertAndSend(exchange, "", message, correlationData);
        } catch (JsonProcessingException e) {
            log.warn(e.toString());
            log.warn(Arrays.toString(e.getStackTrace()));
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
