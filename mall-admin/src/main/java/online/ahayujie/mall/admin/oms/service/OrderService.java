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
     * 创建订单成功后，发送延迟消息到消息队列。
     *
     * @see OrderPublisher#publishOrderTimeoutCancelDelayedMsg(OrderCancelMsgDTO)
     * @param param 订单信息
     */
    void createOrder(CreateOrderParam param);

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
