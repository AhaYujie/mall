package online.ahayujie.mall.admin.oms.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.config.RabbitmqConfig;
import online.ahayujie.mall.admin.oms.bean.dto.OrderConfirmReceiveMsgDTO;
import online.ahayujie.mall.admin.oms.bean.model.Order;
import online.ahayujie.mall.admin.oms.mapper.OrderMapper;
import online.ahayujie.mall.admin.oms.publisher.OrderPublisher;
import online.ahayujie.mall.admin.oms.service.AbstractOrderState;
import online.ahayujie.mall.admin.oms.service.OrderContext;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 已发货订单状态 Service
 * @author aha
 * @since 2020/8/16
 */
@Slf4j
@Service(value = Order.DELIVERED_STATUS_NAME)
public class DeliveredOrderState extends AbstractOrderState {
    private final OrderMapper orderMapper;
    private final OrderPublisher orderPublisher;

    public DeliveredOrderState(ApplicationContext applicationContext, OrderMapper orderMapper, OrderPublisher orderPublisher) {
        super(applicationContext);
        this.orderMapper = orderMapper;
        this.orderPublisher = orderPublisher;
    }

    /**
     * 订单确认收货。
     * 操作成功后订单状态变为 {@link Order.Status#UN_COMMENT}。
     * 操作成功后发送消息到消息队列
     *
     * @see OrderPublisher#publishConfirmReceiveMsg(OrderConfirmReceiveMsgDTO)
     * @param orderContext orderContext
     * @param orderId 订单id
     * @throws UnsupportedOperationException 当前订单状态不支持此操作
     */
    @Override
    public void confirmReceive(OrderContext orderContext, Long orderId) throws UnsupportedOperationException {
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(Order.Status.UN_COMMENT.getValue());
        order.setUpdateTime(new Date());
        order.setReceiveTime(new Date());
        orderMapper.updateById(order);
        OrderConfirmReceiveMsgDTO msgDTO = new OrderConfirmReceiveMsgDTO(orderId);
        orderPublisher.publishConfirmReceiveMsg(msgDTO);
        orderContext.setOrderState(getOrderState(Order.Status.UN_COMMENT));
    }
}
