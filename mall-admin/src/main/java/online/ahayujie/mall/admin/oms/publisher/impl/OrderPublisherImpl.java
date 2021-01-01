package online.ahayujie.mall.admin.oms.publisher.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.config.RabbitmqConfig;
import online.ahayujie.mall.admin.oms.bean.dto.*;
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

    private final ObjectMapper objectMapper;
    private final RabbitTemplate rabbitTemplate;

    public OrderPublisherImpl(ObjectMapper objectMapper, RabbitTemplate rabbitTemplate) {
        this.objectMapper = objectMapper;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Async
    @Override
    public void publishOrderTimeoutCancelDelayedMsg(OrderCancelMsgDTO orderCancelMsgDTO, int timeout) {
        try {
            String message = objectMapper.writeValueAsString(orderCancelMsgDTO);
            String exchange = RabbitmqConfig.ORDER_TTL_EXCHANGE;
            String routingKey = RabbitmqConfig.ORDER_CANCEL_TTL_ROUTING_KEY;
            CorrelationData correlationData = mqService.generateCorrelationData(exchange, routingKey, message);
            rabbitTemplate.convertAndSend(exchange, routingKey, message, msg -> {
                msg.getMessageProperties().setExpiration(Integer.toString(timeout));
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

    @Async
    @Override
    public void publishRefundApplyRefusedMsg(OrderRefundApplyRefusedMsgDTO msgDTO) {
        try {
            String message = objectMapper.writeValueAsString(msgDTO);
            String exchange = RabbitmqConfig.ORDER_REFUND_APPLY_REFUSED_EXCHANGE;
            CorrelationData correlationData = mqService.generateCorrelationData(exchange, "", message);
            rabbitTemplate.convertAndSend(exchange, "", message, correlationData);
        } catch (JsonProcessingException e) {
            log.warn(e.toString());
            log.warn(Arrays.toString(e.getStackTrace()));
        }
    }

    @Async
    @Override
    public void publishReturnApplyRefusedMsg(OrderReturnApplyRefusedMsgDTO msgDTO) {
        try {
            String message = objectMapper.writeValueAsString(msgDTO);
            String exchange = RabbitmqConfig.ORDER_RETURN_APPLY_REFUSED_EXCHANGE;
            CorrelationData correlationData = mqService.generateCorrelationData(exchange, "", message);
            rabbitTemplate.convertAndSend(exchange, "", message, correlationData);
        } catch (JsonProcessingException e) {
            log.warn(e.toString());
            log.warn(Arrays.toString(e.getStackTrace()));
        }
    }

    @Async
    @Override
    public void publishRefundApplyAgreeMsg(OrderRefundApplyAgreeMsgDTO msgDTO) {
        try {
            String message = objectMapper.writeValueAsString(msgDTO);
            String exchange = RabbitmqConfig.ORDER_REFUND_APPLY_AGREE_EXCHANGE;
            CorrelationData correlationData = mqService.generateCorrelationData(exchange, "", message);
            rabbitTemplate.convertAndSend(exchange, "", message, correlationData);
        } catch (JsonProcessingException e) {
            log.warn(e.toString());
            log.warn(Arrays.toString(e.getStackTrace()));
        }
    }

    @Async
    @Override
    public void publishReturnApplyAgreeMsg(OrderReturnApplyAgreeMsgDTO msgDTO) {
        try {
            String message = objectMapper.writeValueAsString(msgDTO);
            String exchange = RabbitmqConfig.ORDER_RETURN_APPLY_AGREE_EXCHANGE;
            CorrelationData correlationData = mqService.generateCorrelationData(exchange, "", message);
            rabbitTemplate.convertAndSend(exchange, "", message, correlationData);
        } catch (JsonProcessingException e) {
            log.warn(e.toString());
            log.warn(Arrays.toString(e.getStackTrace()));
        }
    }

    @Async
    @Override
    public void publishRefundCompleteMsg(OrderRefundCompleteMsgDTO msgDTO) {
        try {
            String message = objectMapper.writeValueAsString(msgDTO);
            String exchange = RabbitmqConfig.ORDER_REFUND_COMPLETE_EXCHANGE;
            CorrelationData correlationData = mqService.generateCorrelationData(exchange, "", message);
            rabbitTemplate.convertAndSend(exchange, "", message, correlationData);
        } catch (JsonProcessingException e) {
            log.warn(e.toString());
            log.warn(Arrays.toString(e.getStackTrace()));
        }
    }

    @Async
    @Override
    public void publishReturnCompleteMsg(OrderReturnCompleteMsgDTO msgDTO) {
        try {
            String message = objectMapper.writeValueAsString(msgDTO);
            String exchange = RabbitmqConfig.ORDER_RETURN_COMPLETE_EXCHANGE;
            CorrelationData correlationData = mqService.generateCorrelationData(exchange, "", message);
            rabbitTemplate.convertAndSend(exchange, "", message, correlationData);
        } catch (JsonProcessingException e) {
            log.warn(e.toString());
            log.warn(Arrays.toString(e.getStackTrace()));
        }
    }

    @Async
    @Override
    public void publishConfirmReceiveMsg(OrderConfirmReceiveMsgDTO msgDTO) {
        try {
            String message = objectMapper.writeValueAsString(msgDTO);
            String exchange = RabbitmqConfig.ORDER_CONFIRM_RECEIVE_EXCHANGE;
            CorrelationData correlationData = mqService.generateCorrelationData(exchange, "", message);
            rabbitTemplate.convertAndSend(exchange, "", message, correlationData);
        } catch (JsonProcessingException e) {
            log.warn(e.getMessage());
            log.warn(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void publishCloseMsg(OrderCloseMsgDTO msgDTO) {
        try {
            String message = objectMapper.writeValueAsString(msgDTO);
            String exchange = RabbitmqConfig.ORDER_CLOSE_EXCHANGE;
            CorrelationData correlationData = mqService.generateCorrelationData(exchange, "", message);
            rabbitTemplate.convertAndSend(exchange, "", message, correlationData);
        } catch (JsonProcessingException e) {
            log.warn(e.getMessage());
            log.warn(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void publishCommentMsg(OrderCommentMsgDTO msgDTO) {
        try {
            String message = objectMapper.writeValueAsString(msgDTO);
            String exchange = RabbitmqConfig.ORDER_COMMENT_EXCHANGE;
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
