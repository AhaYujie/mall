package online.ahayujie.mall.admin.oms.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.oms.bean.model.Order;
import online.ahayujie.mall.admin.oms.mapper.OrderMapper;
import online.ahayujie.mall.admin.oms.service.AbstractOrderState;
import online.ahayujie.mall.admin.oms.service.OrderContext;
import online.ahayujie.mall.admin.oms.service.OrderContextFactory;
import online.ahayujie.mall.admin.oms.service.OrderState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 未付款订单状态 Service
 * @author aha
 * @since 2020/8/15
 */
@Slf4j
@Service(value = Order.UN_PAY_STATUS_NAME)
public class UnPayOrderState extends AbstractOrderState {
    private final OrderMapper orderMapper;

    public UnPayOrderState(ApplicationContext applicationContext, OrderMapper orderMapper) {
        super(applicationContext);
        this.orderMapper = orderMapper;
    }

    @Override
    @PostConstruct
    protected void initOrderStateMap() {
        super.initOrderStateMap();
    }

    /**
     * 因为可能在取消订单的同时，用户正在支付订单，所以使用
     * redis分布式锁给订单加锁。
     * 若加锁失败或释放锁失败，则取消关闭订单。
     *
     * @see #memberCancelOrder(OrderContext, Long)
     * @param orderContext orderContext
     * @param id 订单id
     */
    @Override
    public void cancelTimeoutOrder(OrderContext orderContext, Long id) {
        // TODO:给订单加锁
        memberCancelOrder(orderContext, id);
    }

    /**
     * 取消超时未支付的订单，返还用户优惠券和用户积分。
     * 订单取消后状态变为 {@link Order.Status#CLOSED}。
     *
     * @param orderContext orderContext
     * @param id 订单id
     */
    @Override
    public void memberCancelOrder(OrderContext orderContext, Long id) {
        // 更新订单状态
        Order order = new Order();
        order.setId(id);
        order.setStatus(Order.Status.CLOSED.getValue());
        order.setUpdateTime(new Date());
        orderMapper.updateById(order);
        // TODO:返还用户优惠券和用户积分
        orderContext.setOrderState(getOrderState(Order.Status.CLOSED));
    }
}
