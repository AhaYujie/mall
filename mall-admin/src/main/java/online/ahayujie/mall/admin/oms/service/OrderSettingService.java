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
     * 获取发货未确认收货超时自动确认cron设置
     * @return 发货未确认收货超时自动确认cron设置
     */
    String getAutoConfirmReceiveCron();

    /**
     * 设置发货未确认收货超时自动确认cron设置
     * @param cron 发货未确认收货超时自动确认cron设置
     * @throws IllegalArgumentException cron不合法
     */
    void updateAutoConfirmReceiveCron(String cron) throws IllegalArgumentException;

    /**
     * 获取确认收货后未评价超时自动评价cron设置
     * @return 确认收货后未评价超时自动评价cron设置
     */
    String getAutoCommentCron();

    /**
     * 设置确认收货后未评价超时自动评价cron设置
     * @param cron 确认收货后未评价超时自动评价cron设置
     * @throws IllegalArgumentException cron不合法
     */
    void updateAutoCommentCron(String cron) throws IllegalArgumentException;

    /**
     * 获取订单交易完成后自动关闭交易，不能申请售后的cron设置
     * @return 订单交易完成后自动关闭交易，不能申请售后的cron设置
     */
    String getAutoCloseCron();

    /**
     * 设置订单交易完成后自动关闭交易，不能申请售后的cron设置
     * @param cron 订单交易完成后自动关闭交易，不能申请售后的cron设置
     * @throws IllegalArgumentException cron不合法
     */
    void updateAutoCloseCron(String cron) throws IllegalArgumentException;

    /**
     * 获取全部订单设置
     * @return 全部订单设置
     */
    OrderSettingDTO getAll();
}
