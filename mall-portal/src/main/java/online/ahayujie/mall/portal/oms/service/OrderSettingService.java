package online.ahayujie.mall.portal.oms.service;

/**
 * 订单设置 Service
 * @author aha
 * @since 2020/12/18
 */
public interface OrderSettingService {
    /**
     * 获取订单未支付超时关闭的时间，单位分钟
     * @return 订单未支付超时关闭的时间
     */
    Integer getUnPayTimeout();
}
