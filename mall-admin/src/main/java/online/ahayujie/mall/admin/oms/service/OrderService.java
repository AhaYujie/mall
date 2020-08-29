package online.ahayujie.mall.admin.oms.service;

import com.rabbitmq.client.Channel;
import online.ahayujie.mall.admin.config.RabbitmqConfig;
import online.ahayujie.mall.admin.oms.bean.dto.*;
import online.ahayujie.mall.admin.oms.bean.model.Order;
import online.ahayujie.mall.admin.oms.exception.IllegalOrderException;
import online.ahayujie.mall.admin.oms.publisher.OrderPublisher;
import online.ahayujie.mall.common.api.CommonPage;
import org.springframework.amqp.core.Message;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author aha
 * @since 2020-08-08
 */
public interface OrderService {
    /**
     * 分页查询订单列表。
     * 查询参数中某一字段为null则忽略不作为查询条件。
     * 查询参数之间的关系为且。
     * 订单列表按照创建时间从新到旧排序。
     * @param pageNum 页索引
     * @param pageSize 页大小
     * @param param 查询参数
     * @return 订单列表
     */
    CommonPage<OrderListDTO> list(Integer pageNum, Integer pageSize, QueryOrderListParam param);

    /**
     * 获取订单详情。
     * @param id 订单id
     * @return 订单详情
     * @throws IllegalOrderException 订单不存在
     */
    OrderDetailDTO getOrderDetail(Long id) throws IllegalOrderException;

    /**
     * 创建订单。
     * 优惠方式只支持管理员后台调整订单使用的折扣金额。
     * 订单收货地址为会员的默认收货地址，如果不存在默认收货地址则不设置订单收货地址。
     * 创建订单成功后，发送延迟消息到消息队列。
     *
     * @see OrderPublisher#publishOrderTimeoutCancelDelayedMsg(OrderCancelMsgDTO)
     * @param param 订单信息
     * @throws IllegalOrderException {@code param} 不合法
     */
    void createOrder(CreateOrderParam param) throws IllegalOrderException;

    /**
     * 生成订单号。
     * 生成规则：日期(yyyyMMddHH格式) + 2位随机数 + 2位订单来源(不足2位补0)
     * + 2位订单类型(不足2位补0) + 2位随机数 + 6位以上自增id(不足6位补0)。
     * 自增id：每天从0开始递增，通过redis原子操作获取。
     *
     * @param order 订单信息
     * @return 订单号
     */
    String generateOrderSn(Order order);

    /**
     * 订单发货。
     * 操作完成后发送消息到消息队列。
     *
     * @param param 发货信息
     * @throws IllegalOrderException 订单不存在
     * @throws UnsupportedOperationException 订单不支持此操作
     */
    void deliverOrder(DeliverOrderParam param) throws IllegalOrderException, UnsupportedOperationException;

    /**
     * 拒绝订单售后申请
     * @param orderId 订单id
     * @param orderProductIds 售后的订单商品id
     * @throws UnsupportedOperationException 当前订单不支持此操作
     */
    void refuseAfterSaleApply(Long orderId, List<Long> orderProductIds) throws UnsupportedOperationException;

    /**
     * 同意订单售后申请
     * @param orderId 订单id
     * @throws UnsupportedOperationException 当前订单不支持此操作
     */
    void agreeAfterSaleApply(Long orderId) throws UnsupportedOperationException;

    /**
     * 监听订单超时未支付自动取消消息。
     * 需要保证幂等性，避免重复消费消息导致商品库存，优惠券，积分等数据不一致。
     * 相比 {@link #listenMemberCancel(Channel, Message)} 接口，需要考虑
     * 可能出现在取消超时订单的同时用户支付订单的情况。
     *
     * @param channel channel
     * @param message message
     * @throws IOException 确认消息失败
     */
    void listenTimeoutCancel(Channel channel, Message message) throws IOException;

    /**
     * 监听用户取消订单消息。
     * 需要保证幂等性，避免重复消费消息导致商品库存，优惠券，积分等数据不一致。
     * @param channel channel
     * @param message message
     * @throws IOException 确认消息失败
     */
    void listenMemberCancel(Channel channel, Message message) throws IOException;
}
