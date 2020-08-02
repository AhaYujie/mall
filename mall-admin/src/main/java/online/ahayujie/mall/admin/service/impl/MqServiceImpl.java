package online.ahayujie.mall.admin.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.service.MqService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

/**
 * 消息队列处理者。
 * 负责发送消息异常时的重试机制。
 * @author aha
 * @since 2020/7/29
 */
@Slf4j
@Service
public class MqServiceImpl implements MqService {
    private static final int RETRY = 10;

    private final ObjectMapper objectMapper;
    private final RabbitTemplate rabbitTemplate;

    public MqServiceImpl(ObjectMapper objectMapper, RabbitTemplate rabbitTemplate) {
        this.objectMapper = objectMapper;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        // 发送成功和correlationData为null不需要重发消息
        if (ack || correlationData == null) {
            return;
        }
        try {
            CorrelationDataDTO correlationDataDTO = objectMapper.readValue(correlationData.getId(), CorrelationDataDTO.class);
            Integer retry = correlationDataDTO.getRetry();
            if (retry < 0) {
                log.error("发送消息失败原因: " + cause);
                log.error(String.format("重发消息(%s)失败超过%d次", correlationData, RETRY));
                return;
            }
            correlationDataDTO.setRetry(retry - 1);
            CorrelationData newCorrelationData = generateCorrelationData(correlationDataDTO.getExchange(),
                    correlationDataDTO.getRoutingKey(), correlationDataDTO.getMessage(), correlationDataDTO.getRetry());
            rabbitTemplate.convertAndSend(correlationDataDTO.getExchange(), correlationDataDTO.getRoutingKey(),
                    correlationDataDTO.getMessage(), newCorrelationData);
        } catch (JsonProcessingException e) {
            log.warn(e.getMessage());
            log.warn(Arrays.toString(e.getStackTrace()));
        }

    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.warn(String.format("消息未到达队列, message: %s, replyCode: %d, replyText: %s, exchange: %s, routingKey: %s",
                message, replyCode, replyText, exchange, routingKey));
    }

    @Override
    public CorrelationData generateCorrelationData(String exchange, String routingKey, Serializable message) {
        return generateCorrelationData(exchange, routingKey, message, RETRY);
    }

    private CorrelationData generateCorrelationData(String exchange, String routingKey, Serializable message, Integer retry) {
        CorrelationDataDTO correlationDataDTO = new CorrelationDataDTO(exchange, routingKey, message, retry);
        try {
            return new CorrelationData(objectMapper.writeValueAsString(correlationDataDTO));
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @Data
    private static class CorrelationDataDTO implements Serializable {
        private String exchange;
        private String routingKey;
        private Serializable message;
        private Integer retry;

        public CorrelationDataDTO() {
        }

        public CorrelationDataDTO(String exchange, String routingKey, Serializable message, Integer retry) {
            this.exchange = exchange;
            this.routingKey = routingKey;
            this.message = message;
            this.retry = retry;
        }
    }
}
