package online.ahayujie.mall.admin.pms.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * 消息队列处理者。
 * 负责发送消息异常时的重试机制。
 * @author aha
 * @since 2020/7/29
 */
@Slf4j
@Service
public class MqHandler implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {
    private final RabbitTemplate rabbitTemplate;

    public MqHandler(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.debug("correlationData: " + correlationData);
        log.debug("ack: " + ack);
        log.debug("cause: " + cause);
        // TODO:处理消息未到达Broker
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.debug("message: " + message);
        log.debug("replyCode: " + replyCode);
        log.debug("replyText: " + replyText);
        log.debug("exchange: " + exchange);
        log.debug("routingKey: " + routingKey);
        // TODO:处理消息已到达Broker，未到达队列
    }
}
