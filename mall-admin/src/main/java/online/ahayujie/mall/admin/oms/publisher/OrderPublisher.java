package online.ahayujie.mall.admin.oms.publisher;

import online.ahayujie.mall.admin.config.RabbitmqConfig;
import online.ahayujie.mall.admin.oms.bean.dto.*;

/**
 * 订单消息发送者
 * @author aha
 * @since 2020/8/16
 */
public interface OrderPublisher {
    /**
     * 发送延迟消息到消息队列 {@link RabbitmqConfig#ORDER_CANCEL_TTL_QUEUE}，
     * 消息到期后会重新入队到超时取消订单队列 {@link RabbitmqConfig#ORDER_TIMEOUT_CANCEL_QUEUE}。
     *
     * @param orderCancelMsgDTO 消息内容
     * @param timeout 订单消息过期时间, 单位毫秒
     */
    void publishOrderTimeoutCancelDelayedMsg(OrderCancelMsgDTO orderCancelMsgDTO, int timeout);

    /**
     * 发送订单已取消消息到消息队列，
     * exchange为 {@link RabbitmqConfig#ORDER_CANCELLED_EXCHANGE}。
     *
     * @param orderCancelledMsgDTO 订单已取消消息
     */
    void publishOrderCancelledMsg(OrderCancelledMsgDTO orderCancelledMsgDTO);

    /**
     * 发送订单发货消息到消息队列，
     * exchange为 {@link RabbitmqConfig#ORDER_DELIVER_EXCHANGE}。
     *
     * @param orderDeliverMsgDTO 订单发货消息
     */
    void publishOrderDeliverMsg(OrderDeliverMsgDTO orderDeliverMsgDTO);

    /**
     * 发送订单仅退款申请被拒绝消息到消息队列。
     * exchange为 {@link RabbitmqConfig#ORDER_REFUND_APPLY_REFUSED_EXCHANGE}
     *
     * @param msgDTO 订单仅退款申请被拒绝消息
     */
    void publishRefundApplyRefusedMsg(OrderRefundApplyRefusedMsgDTO msgDTO);

    /**
     * 发送订单退货退款申请被拒绝消息到消息队列。
     * exchange为 {@link RabbitmqConfig#ORDER_RETURN_APPLY_REFUSED_EXCHANGE}
     *
     * @param msgDTO 订单退货退款申请被拒绝消息
     */
    void publishReturnApplyRefusedMsg(OrderReturnApplyRefusedMsgDTO msgDTO);

    /**
     * 发送同意订单仅退款申请消息到消息队列。
     * exchange为 {@link RabbitmqConfig#ORDER_REFUND_APPLY_AGREE_EXCHANGE}
     *
     * @param msgDTO 同意订单仅退款申请消息
     */
    void publishRefundApplyAgreeMsg(OrderRefundApplyAgreeMsgDTO msgDTO);

    /**
     * 发送同意订单退货退款申请消息到消息队列。
     * exchange为 {@link RabbitmqConfig#ORDER_RETURN_APPLY_AGREE_EXCHANGE}
     *
     * @param msgDTO 同意订单退货退款申请消息
     */
    void publishReturnApplyAgreeMsg(OrderReturnApplyAgreeMsgDTO msgDTO);

    /**
     * 发送完成订单仅退款消息到消息队列。
     * exchange为 {@link RabbitmqConfig#ORDER_REFUND_COMPLETE_EXCHANGE}
     *
     * @param msgDTO 订单仅退款消息
     */
    void publishRefundCompleteMsg(OrderRefundCompleteMsgDTO msgDTO);

    /**
     * 发送完成订单退货退款消息到消息队列。
     * exchange为 {@link RabbitmqConfig#ORDER_RETURN_COMPLETE_EXCHANGE}
     *
     * @param msgDTO 完成订单退货退款消息
     */
    void publishReturnCompleteMsg(OrderReturnCompleteMsgDTO msgDTO);

    /**
     * 发送订单确认收货消息到消息队列。
     * exchange为 {@link RabbitmqConfig#ORDER_CONFIRM_RECEIVE_EXCHANGE}
     *
     * @param msgDTO 订单确认收货消息
     */
    void publishConfirmReceiveMsg(OrderConfirmReceiveMsgDTO msgDTO);

    /**
     * 发送订单关闭消息到消息队列
     * @param msgDTO 订单关闭消息
     */
    void publishCloseMsg(OrderCloseMsgDTO msgDTO);

    /**
     * 发送评价订单消息到消息队列
     * @param msgDTO 评价订单消息
     */
    void publishCommentMsg(OrderCommentMsgDTO msgDTO);
}
