package online.ahayujie.mall.admin.oms.publisher;

import online.ahayujie.mall.admin.config.RabbitmqConfig;
import online.ahayujie.mall.admin.oms.bean.dto.OrderCancelMsgDTO;

/**
 * 订单消息发送者
 * @author aha
 * @since 2020/8/16
 */
public interface OrderPublisher {
    /**
     * 发送延迟消息到消息队列 {@link RabbitmqConfig#ORDER_CANCEL_TTL_QUEUE}，
     * 消息到期后会重新入队到超时取消订单队列 {@link RabbitmqConfig#ORDER_TIMEOUT_CANCEL_QUEUE}。
     *
     * @param orderCancelMsgDTO 消息内容
     */
    void publishOrderTimeoutCancelDelayedMsg(OrderCancelMsgDTO orderCancelMsgDTO);
}
