package online.ahayujie.mall.admin.oms.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.config.RabbitmqConfig;
import online.ahayujie.mall.admin.oms.bean.dto.OrderCloseMsgDTO;
import online.ahayujie.mall.admin.oms.bean.model.Order;
import online.ahayujie.mall.admin.oms.mapper.OrderMapper;
import online.ahayujie.mall.admin.oms.publisher.OrderPublisher;
import online.ahayujie.mall.admin.oms.service.AbstractOrderState;
import online.ahayujie.mall.admin.oms.service.OrderContext;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 交易完成订单状态 Service
 * @author aha
 * @since 2020/8/16
 */
@Slf4j
@Service(value = Order.COMPLETE_STATUS_NAME)
public class CompleteOrderState extends AbstractOrderState {
    private final OrderMapper orderMapper;
    private final OrderPublisher orderPublisher;

    public CompleteOrderState(ApplicationContext applicationContext, OrderMapper orderMapper, OrderPublisher orderPublisher) {
        super(applicationContext);
        this.orderMapper = orderMapper;
        this.orderPublisher = orderPublisher;
    }

    /**
     * 关闭订单。
     * 操作成功后发送消息到消息队列, exchange为 {@link RabbitmqConfig#ORDER_CLOSE_EXCHANGE}
     * 操作成功后订单状态变为 {@link Order.Status#CLOSED}。
     *
     * @param orderContext orderContext
     * @param orderId 订单id
     * @throws UnsupportedOperationException 当前订单状态不支持此操作
     */
    @Override
    public void closeOrder(OrderContext orderContext, Long orderId) throws UnsupportedOperationException {
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(Order.Status.CLOSED.getValue());
        order.setUpdateTime(new Date());
        orderMapper.updateById(order);
        orderPublisher.publishCloseMsg(new OrderCloseMsgDTO(orderId));
        orderContext.setOrderState(getOrderState(Order.Status.CLOSED));
    }
}
