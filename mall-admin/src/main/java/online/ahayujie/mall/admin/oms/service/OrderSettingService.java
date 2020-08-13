package online.ahayujie.mall.admin.oms.service;

import online.ahayujie.mall.admin.oms.bean.dto.OrderSettingDTO;

/**
 * 订单设置 Service
 * @author aha
 * @since 2020/8/12
 */
public interface OrderSettingService {
    /**
     * 获取订单未支付超时关闭的时间，单位分钟
     * @return 订单未支付超时关闭的时间
     */
    Integer getUnPayTimeout();

    /**
     * 设置订单未支付超时关闭的时间，单位分钟
     * @param time 订单未支付超时关闭的时间
     * @throws IllegalArgumentException 订单未支付超时关闭的时间不合法
     */
    void updateUnPayTimeOut(Integer time) throws IllegalArgumentException;

    /**
     * 获取发货未确认收货超时自动确认时间(单位天)
     * @return 发货未确认收货超时自动确认时间
     */
    Integer getAutoConfirmReceiveTime();

    /**
     * 设置发货未确认收货超时自动确认时间(单位天)
     * @param time 发货未确认收货超时自动确认时间
     * @throws IllegalArgumentException 时间不合法
     */
    void updateAutoConfirmReceiveTime(Integer time) throws IllegalArgumentException;

    /**
     * 获取确认收货后未评价超时自动评价时间(单位天)
     * @return 确认收货后未评价超时自动评价时间
     */
    Integer getAutoCommentTime();

    /**
     * 设置确认收货后未评价超时自动评价时间(单位天)
     * @param time 确认收货后未评价超时自动评价时间
     * @throws IllegalArgumentException 时间不合法
     */
    void updateAutoCommentTime(Integer time) throws IllegalArgumentException;

    /**
     * 获取订单交易完成后自动关闭交易，不能申请售后的时间(单位天)
     * @return 获取订单交易完成后自动关闭交易，不能申请售后的时间
     */
    Integer getAutoCloseTime();

    /**
     * 设置订单交易完成后自动关闭交易，不能申请售后的时间(单位天)
     * @param time 订单交易完成后自动关闭交易，不能申请售后的时间
     * @throws IllegalArgumentException 时间不合法
     */
    void updateAutoCloseTime(Integer time) throws IllegalArgumentException;

    /**
     * 获取全部订单设置
     * @return 全部订单设置
     */
    OrderSettingDTO getAll();
}
