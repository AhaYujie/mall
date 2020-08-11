package online.ahayujie.mall.admin.oms.service;

import online.ahayujie.mall.admin.oms.bean.model.Order;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 订单Context工厂
 * @author aha
 * @since 2020/8/8
 */
@Service
public class OrderContextFactory {
    private final Map<String, OrderState> orderStateMap;

    public OrderContextFactory(Map<String, OrderState> orderStateMap) {
        this.orderStateMap = orderStateMap;
    }

    /**
     * 根据订单状态值获取相应的订单Context
     * @param status 订单状态值
     * @return 订单Context
     */
    public OrderContext getOrderContext(Integer status) {
        OrderState orderState = getOrderState(status);
        return new OrderContext(orderState);
    }

    /**
     * 根据订单状态获取相应的订单Context
     * @param status 订单状态
     * @return 订单Context
     */
    public OrderContext getOrderContext(Order.Status status) {
        OrderState orderState = getOrderState(status);
        return new OrderContext(orderState);
    }

    /**
     * 根据订单状态值获取订单状态实现类。
     * 若订单状态不存在则返回null。
     * @param statusValue 订单状态值
     * @return 订单状态实现类
     */
    public OrderState getOrderState(Integer statusValue) {
        for (Order.Status status : Order.Status.values()) {
            if (status.getValue().equals(statusValue)) {
                return orderStateMap.get(status.getName());
            }
        }
        return null;
    }

    /**
     * 获取订单状态实现类
     * @param status 订单状态
     * @return 订单状态实现类
     */
    public OrderState getOrderState(Order.Status status) {
        return orderStateMap.get(status.getName());
    }
}
