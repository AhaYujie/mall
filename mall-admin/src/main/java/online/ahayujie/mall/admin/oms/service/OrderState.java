package online.ahayujie.mall.admin.oms.service;

import online.ahayujie.mall.admin.oms.bean.dto.DeliverOrderParam;

/**
 * 状态模式实现不同状态订单的业务逻辑。
 * 每一个订单状态实现类是单例模式。
 * @author aha
 * @since 2020/8/8
 */
public interface OrderState {
    /**
     * 取消超时未支付的订单。
     * 需要考虑取消订单的同时用户可能在支付订单。
     * 取消订单成功后，发送消息到消息队列。
     *
     * @param orderContext orderContext
     * @param id 订单id
     * @throws UnsupportedOperationException 当前订单状态不支持此操作
     */
    void cancelTimeoutOrder(OrderContext orderContext, Long id) throws UnsupportedOperationException;

    /**
     * 用户取消未支付的订单。
     * 取消订单成功后，发送消息到消息队列。
     *
     * @param orderContext orderContext
     * @param id 订单id
     * @throws UnsupportedOperationException 当前订单状态不支持此操作
     */
    void memberCancelOrder(OrderContext orderContext, Long id) throws UnsupportedOperationException;

    /**
     * 订单发货。
     * 订单发货成功后，发送消息到消息队列。
     *
     * @param orderContext orderContext
     * @param param 发货信息
     * @throws UnsupportedOperationException 当前订单状态不支持此操作
     */
    void deliverOrder(OrderContext orderContext, DeliverOrderParam param) throws UnsupportedOperationException;
}
