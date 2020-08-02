package online.ahayujie.mall.admin.service;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.Serializable;

/**
 * 消息队列 Service
 * @author aha
 * @since 2020/8/1
 */
public interface MqService extends RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {
    /**
     * 生成CorrelationData，给confirm的重发机制使用。
     * 若生成失败则返回null。
     * @param exchange 交换机
     * @param routingKey 路由键
     * @param message 消息
     * @return CorrelationData
     */
    CorrelationData generateCorrelationData(String exchange, String routingKey, Serializable message);
}
