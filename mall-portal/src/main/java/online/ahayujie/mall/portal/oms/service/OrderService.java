package online.ahayujie.mall.portal.oms.service;

import online.ahayujie.mall.portal.oms.bean.dto.*;
import online.ahayujie.mall.portal.oms.bean.model.Order;
import online.ahayujie.mall.portal.oms.publisher.OrderPublisher;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author aha
 * @since 2020-11-07
 */
public interface OrderService {
    /**
     * 生成确认订单信息。
     * 此阶段不对商品库存进行校验。
     *
     * @param param 订单参数
     * @return 确认订单信息
     * @throws IllegalArgumentException 参数不合法
     */
    ConfirmOrderDTO generateConfirmOrder(GenerateConfirmOrderParam param) throws IllegalArgumentException;

    /**
     * 获取积分规则
     * @return 积分规则
     */
    IntegrationRule getIntegrationRule();

    /**
     * 提交订单。
     * <li> 检验参数是否合法
     * <li> 扣减商品库存
     * <li> 扣除会员使用的积分
     * <li> 生成订单
     * <p>
     * 如果上述某一步骤失败，则生成订单失败，回滚所有操作。
     * 生成订单成功后，调用 {@link OrderPublisher#publishOrderTimeoutCancelDelayedMsg(OrderCancelMsgDTO)}
     * 发送延迟消息到消息队列。
     *
     * @param param 订单参数
     * @return 生成订单的信息
     * @throws IllegalArgumentException 订单参数不合法
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    SubmitOrderDTO submit(SubmitOrderParam param) throws IllegalArgumentException;

    /**
     * 生成订单号。
     * 生成规则：日期(yyyyMMddHH格式) + 2位随机数 + 2位订单来源(不足2位补0)
     * + 2位订单类型(不足2位补0) + 2位随机数 + 6位以上自增id(不足6位补0)。
     * 自增id：每天从0开始递增，通过redis原子操作获取。
     *
     * @param order 订单信息，需要包含订单来源和订单类型
     * @return 订单号
     */
    String generateOrderSn(Order order);
}
