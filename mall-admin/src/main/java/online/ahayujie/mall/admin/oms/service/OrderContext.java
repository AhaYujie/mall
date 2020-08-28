package online.ahayujie.mall.admin.oms.service;

import online.ahayujie.mall.admin.oms.bean.dto.DeliverOrderParam;

import java.util.List;

/**
 * 状态模式下的订单Context类。
 * @author aha
 * @since 2020/8/8
 */
public class OrderContext {
    private OrderState orderState;

    public OrderContext(OrderState orderState) {
        this.orderState = orderState;
    }

    /**
     * 取消超时未支付的订单。
     * 
     * @see OrderState#cancelTimeoutOrder(OrderContext, Long) 
     * @param id 订单id
     * @throws UnsupportedOperationException 当前订单状态不支持此操作
     */
    public void cancelTimeoutOrder(Long id) throws UnsupportedOperationException {
        orderState.cancelTimeoutOrder(this, id);
    }

    /**
     * 用户取消未支付的订单。
     * 
     * @see OrderState#memberCancelOrder(OrderContext, Long) 
     * @param id 订单id
     * @throws UnsupportedOperationException 当前订单状态不支持此操作
     */ 
    public void memberCancelOrder(Long id) throws UnsupportedOperationException {
        orderState.memberCancelOrder(this, id);
    }

    /**
     * 订单发货。
     * 订单发货成功后，发送消息到消息队列。
     *
     * @param param 发货信息
     * @throws UnsupportedOperationException 当前订单状态不支持此操作
     */
    public void deliverOrder(DeliverOrderParam param) throws UnsupportedOperationException {
        orderState.deliverOrder(this, param);
    }

    /**
     * 拒绝订单售后申请
     * @param orderId 订单id
     * @param orderProductIds 售后的订单商品id
     * @throws UnsupportedOperationException 当前订单状态不支持此操作
     */
    public void refuseAfterSaleApply(Long orderId, List<Long> orderProductIds) throws UnsupportedOperationException {
        orderState.refuseAfterSaleApply(this, orderId, orderProductIds);
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    public OrderState getOrderState() {
        return orderState;
    }
}
