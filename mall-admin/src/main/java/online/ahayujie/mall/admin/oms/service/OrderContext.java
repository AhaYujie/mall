package online.ahayujie.mall.admin.oms.service;

/**
 * 状态模式下的订单Context类。
 * @author aha
 * @since 2020/8/8
 */
public class OrderContext implements OrderState {
    private OrderState orderState;

    public OrderContext(OrderState orderState) {
        this.orderState = orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }
}
