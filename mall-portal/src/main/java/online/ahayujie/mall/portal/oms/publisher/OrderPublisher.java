package online.ahayujie.mall.portal.oms.publisher;

import online.ahayujie.mall.portal.config.RabbitmqConfig;
import online.ahayujie.mall.portal.oms.bean.dto.OrderCancelMsgDTO;
import online.ahayujie.mall.portal.oms.service.OrderSettingService;

/**
 * 订单消息发送者
 * @author aha
 * @since 2020/12/18
 */
public interface OrderPublisher {
    /**
     * 发送延迟消息到消息队列 {@link RabbitmqConfig#ORDER_CANCEL_TTL_QUEUE}，
     * 消息到期后会重新入队到超时取消订单队列 {@link RabbitmqConfig#ORDER_TIMEOUT_CANCEL_QUEUE}。
     * 消息过期时间通过 {@link OrderSettingService#getUnPayTimeout()} 获取。
     *
     * @param orderCancelMsgDTO 消息内容
     */
    void publishOrderTimeoutCancelDelayedMsg(OrderCancelMsgDTO orderCancelMsgDTO);
}
