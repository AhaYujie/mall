package online.ahayujie.mall.portal.oms.service;

import online.ahayujie.mall.portal.oms.bean.dto.ConfirmOrderDTO;
import online.ahayujie.mall.portal.oms.bean.dto.GenerateConfirmOrderParam;
import online.ahayujie.mall.portal.oms.bean.dto.IntegrationRule;

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
}
