package online.ahayujie.mall.admin.oms.service;

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
     */
    public void cancelTimeoutOrder(Long id) {
        orderState.cancelTimeoutOrder(this, id);
    }

    /**
     * 用户取消未支付的订单。
     * 
     * @see OrderState#memberCancelOrder(OrderContext, Long) 
     * @param id 订单id
     */ 
    public void memberCancelOrder(Long id) {
        orderState.memberCancelOrder(this, id);
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    public OrderState getOrderState() {
        return orderState;
    }
}
