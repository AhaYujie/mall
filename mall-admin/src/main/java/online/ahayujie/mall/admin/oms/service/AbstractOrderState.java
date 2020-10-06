package online.ahayujie.mall.admin.oms.service;

import online.ahayujie.mall.admin.oms.bean.dto.DeliverOrderParam;
import online.ahayujie.mall.admin.oms.bean.model.Order;
import online.ahayujie.mall.admin.oms.service.impl.ClosedOrderState;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单状态骨架类。
 * 此骨架类主要初始化订单状态实现类Map，
 * 并给子类提供根据名称和值获取订单实现类的方法。
 * 子类需要在初始化时通过Spring注入依赖 {@link ApplicationContext}
 * 和在初始化完成后调用 {@link #initOrderStateMap()} 初始化 {@link #orderStateMap}。
 *
 * @author aha
 * @since 2020/8/15
 */
public abstract class AbstractOrderState implements OrderState {
    protected ApplicationContext applicationContext;

    /**
     * 包含了所有订单实现类的map。
     * key为订单状态实现类的beanId，value为相应的订单状态实现类。
     */
    protected static Map<String, OrderState> orderStateMap;

    /**
     * 子类在初始化时通过Spring注入 {@link #applicationContext} 依赖。
     * @param applicationContext applicationContext
     */
    protected AbstractOrderState(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 初始化 {@link #orderStateMap}。
     * 因为 {@link #orderStateMap} 依赖所有订单状态实现类，所以需要子类在初始化完成后调用此方法。
     * 可能会有多个子类调用此方法，因此需要校验。
     */
    protected void initOrderStateMap() {
        if (orderStateMap == null) {
            synchronized (this) {
                if (orderStateMap == null) {
                    orderStateMap = new HashMap<>();
                    orderStateMap.put(Order.UN_PAY_STATUS_NAME, (OrderState) applicationContext.getBean(Order.UN_PAY_STATUS_NAME));
                    orderStateMap.put(Order.UN_DELIVER_STATUS_NAME, (OrderState) applicationContext.getBean(Order.UN_DELIVER_STATUS_NAME));
                    orderStateMap.put(Order.DELIVERED_STATUS_NAME, (OrderState) applicationContext.getBean(Order.DELIVERED_STATUS_NAME));
                    orderStateMap.put(Order.UN_COMMENT_STATUS_NAME, (OrderState) applicationContext.getBean(Order.UN_COMMENT_STATUS_NAME));
                    orderStateMap.put(Order.COMPLETE_STATUS_NAME, (OrderState) applicationContext.getBean(Order.COMPLETE_STATUS_NAME));
                    orderStateMap.put(Order.APPLY_REFUND_STATUS_NAME, (OrderState) applicationContext.getBean(Order.APPLY_REFUND_STATUS_NAME));
                    orderStateMap.put(Order.APPLY_RETURN_STATUS_NAME, (OrderState) applicationContext.getBean(Order.APPLY_RETURN_STATUS_NAME));
                    orderStateMap.put(Order.REFUND_STATUS_NAME, (OrderState) applicationContext.getBean(Order.REFUND_STATUS_NAME));
                    orderStateMap.put(Order.RETURN_STATUS_NAME, (OrderState) applicationContext.getBean(Order.RETURN_STATUS_NAME));
                    orderStateMap.put(Order.CLOSED_STATUS_NAME, (OrderState) applicationContext.getBean(Order.CLOSED_STATUS_NAME));
                }
            }
        }
    }

    /**
     * 根据订单状态值获取订单状态实现类。
     * 若订单状态不存在则返回null。
     * @param statusValue 订单状态值
     * @return 订单状态实现类
     */
    protected OrderState getOrderState(Integer statusValue) {
        for (Order.Status status : Order.Status.values()) {
            if (status.getValue().equals(statusValue)) {
                return orderStateMap.get(status.getName());
            }
        }
        return null;
    }

    /**
     * 获取订单状态实现类。
     * 若订单状态不存在则返回null。
     * @param status 订单状态
     * @return 订单状态实现类
     */
    protected OrderState getOrderState(Order.Status status) {
        return orderStateMap.get(status.getName());
    }

    @Override
    public void cancelTimeoutOrder(OrderContext orderContext, Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void memberCancelOrder(OrderContext orderContext, Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deliverOrder(OrderContext orderContext, DeliverOrderParam param) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void refuseAfterSaleApply(OrderContext orderContext, Long orderId, List<Long> orderProductIds) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void agreeAfterSaleApply(OrderContext orderContext, Long orderId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void completeAfterSale(OrderContext orderContext, Long orderId, List<Long> orderProductIds) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void confirmReceive(OrderContext orderContext, Long orderId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void closeOrder(OrderContext orderContext, Long orderId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void comment(OrderContext orderContext, Long orderId, List<Long> orderProductIds, String content, String pics, Integer star) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
}
