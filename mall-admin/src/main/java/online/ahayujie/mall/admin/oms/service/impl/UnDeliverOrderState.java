package online.ahayujie.mall.admin.oms.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.config.RabbitmqConfig;
import online.ahayujie.mall.admin.oms.bean.dto.DeliverOrderParam;
import online.ahayujie.mall.admin.oms.bean.dto.OrderDeliverMsgDTO;
import online.ahayujie.mall.admin.oms.bean.model.Order;
import online.ahayujie.mall.admin.oms.mapper.OrderMapper;
import online.ahayujie.mall.admin.oms.publisher.OrderPublisher;
import online.ahayujie.mall.admin.oms.service.AbstractOrderState;
import online.ahayujie.mall.admin.oms.service.OrderContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 待发货订单状态 Service
 * @author aha
 * @since 2020/8/16
 */
@Slf4j
@Service(value = Order.UN_DELIVER_STATUS_NAME)
public class UnDeliverOrderState extends AbstractOrderState {
    private OrderPublisher orderPublisher;

    private final OrderMapper orderMapper;

    public UnDeliverOrderState(ApplicationContext applicationContext, OrderMapper orderMapper) {
        super(applicationContext);
        this.orderMapper = orderMapper;
    }

    /**
     * 订单发货。
     * 订单发货成功后订单状态为 {@link Order.Status#DELIVERED}
     * 订单发货成功后发送消息到消息队列，
     * exchange为 {@link RabbitmqConfig#ORDER_DELIVER_EXCHANGE}。
     *
     * @param orderContext orderContext
     * @param param 发货信息
     */
    @Override
    public void deliverOrder(OrderContext orderContext, DeliverOrderParam param) {
        Order order = new Order();
        BeanUtils.copyProperties(param, order);
        order.setStatus(Order.Status.DELIVERED.getValue());
        order.setUpdateTime(new Date());
        orderMapper.updateById(order);
        OrderDeliverMsgDTO orderDeliverMsgDTO = new OrderDeliverMsgDTO();
        BeanUtils.copyProperties(param, orderDeliverMsgDTO);
        orderPublisher.publishOrderDeliverMsg(orderDeliverMsgDTO);
        orderContext.setOrderState(getOrderState(Order.Status.DELIVERED));
    }

    @Autowired
    public void setOrderPublisher(OrderPublisher orderPublisher) {
        this.orderPublisher = orderPublisher;
    }
}
