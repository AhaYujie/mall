package online.ahayujie.mall.admin.oms.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.oms.bean.model.Order;
import online.ahayujie.mall.admin.oms.bean.model.OrderProduct;
import online.ahayujie.mall.admin.oms.mapper.OrderMapper;
import online.ahayujie.mall.admin.oms.mapper.OrderProductMapper;
import online.ahayujie.mall.admin.oms.service.AbstractOrderState;
import online.ahayujie.mall.admin.oms.service.OrderContext;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 申请仅退款订单状态 Service
 * @author aha
 * @since 2020/8/16
 */
@Slf4j
@Service(value = Order.APPLY_REFUND_STATUS_NAME)
public class ApplyRefundOrderState extends AbstractOrderState {
    private final OrderMapper orderMapper;
    private final OrderProductMapper orderProductMapper;

    public ApplyRefundOrderState(ApplicationContext applicationContext, OrderMapper orderMapper,
                                 OrderProductMapper orderProductMapper) {
        super(applicationContext);
        this.orderMapper = orderMapper;
        this.orderProductMapper = orderProductMapper;
    }

    /**
     * 拒绝订单售后申请。
     * 操作完成后订单状态变为 {@link Order.Status#COMPLETE}
     *
     * @param orderContext orderContext
     * @param orderId 订单id
     * @param orderProductIds 售后的订单商品id
     */
    @Override
    public void refuseAfterSaleApply(OrderContext orderContext, Long orderId, List<Long> orderProductIds) {
        Order order = new Order();
        order.setId(orderId);
        order.setUpdateTime(new Date());
        order.setStatus(Order.Status.COMPLETE.getValue());
        orderMapper.updateById(order);
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setUpdateTime(new Date());
        orderProduct.setStatus(OrderProduct.Status.PAY.getValue());
        for (Long orderProductId : orderProductIds) {
            orderProduct.setId(orderProductId);
            orderProductMapper.updateById(orderProduct);
        }
        orderContext.setOrderState(getOrderState(Order.Status.COMPLETE));
    }

    /**
     * 同意订单售后申请。
     * 操作完成后订单状态变为 {@link Order.Status#REFUND}
     *
     * @param orderContext orderContext
     * @param orderId 订单id
     */
    @Override
    public void agreeAfterSaleApply(OrderContext orderContext, Long orderId) {
        Order order = new Order();
        order.setId(orderId);
        order.setUpdateTime(new Date());
        order.setStatus(Order.Status.REFUND.getValue());
        orderMapper.updateById(order);
        orderContext.setOrderState(getOrderState(Order.Status.REFUND));
    }
}
