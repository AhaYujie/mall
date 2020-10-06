package online.ahayujie.mall.admin.oms.service;

import online.ahayujie.mall.admin.oms.bean.dto.AutoCommentSettingDTO;
import online.ahayujie.mall.admin.oms.bean.dto.OrderSettingDTO;
import org.quartz.SchedulerException;

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
     * @throws SchedulerException 调度定时任务异常
     */
    void updateAutoConfirmReceiveCron(String cron) throws IllegalArgumentException, SchedulerException;

    /**
     * 初始化自动确认收货定时任务
     * @throws SchedulerException 调度定时任务异常
     */
    void initAutoConfirmReceiveJob() throws SchedulerException;

    /**
     * 暂停自动确认收货定时任务
     * @throws SchedulerException 调度定时任务异常
     */
    void pauseAutoConfirmReceiveJob() throws SchedulerException;

    /**
     * 恢复自动确认收货定时任务
     * @throws SchedulerException 调度定时任务异常
     */
    void resumeAutoConfirmReceiveJob() throws SchedulerException;

    /**
     * 删除自动确认收货定时任务
     * @throws SchedulerException 调度定时任务异常
     */
    void deleteAutoConfirmReceiveJob() throws SchedulerException;

    /**
     * 获取发货后经过多少时间未确认收货则自动确认收货(单位秒)
     * @return 时间(单位秒)
     */
    Integer getAutoConfirmReceiveTime();

    /**
     * 更新发货后经过多少时间未确认收货则自动确认收货(单位秒)
     * @param time 时间(单位秒)
     * @throws IllegalArgumentException 时间小于0秒
     */
    void updateAutoConfirmReceiveTime(Integer time) throws IllegalArgumentException;

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
    void updateAutoCommentCron(String cron) throws IllegalArgumentException, SchedulerException;

    /**
     * 初始化自动评价定时任务
     * @throws SchedulerException 调度定时任务异常
     */
    void initAutoCommentJob() throws SchedulerException;

    /**
     * 暂停自动评价定时任务
     * @throws SchedulerException 调度定时任务异常
     */
    void pauseAutoCommentJob() throws SchedulerException;

    /**
     * 恢复自动评价定时任务
     * @throws SchedulerException 调度定时任务异常
     */
    void resumeAutoCommentJob() throws SchedulerException;

    /**
     * 删除自动评价定时任务
     * @throws SchedulerException 调度定时任务异常
     */
    void deleteAutoCommentJob() throws SchedulerException;

    /**
     * 获取确认收货后经过多少时间未评价则自动评价(单位秒)
     * @return 时间(单位秒)
     */
    Integer getAutoCommentTime();

    /**
     * 更新确认收货后经过多少时间未评价则自动评价(单位秒)
     * @param time 时间(单位秒)
     * @throws IllegalArgumentException 时间小于0秒
     */
    void updateAutoCommentTime(Integer time) throws IllegalArgumentException;

    /**
     * 获取自动评价的评价设置
     * @return 自动评价的评价设置
     */
    AutoCommentSettingDTO getAutoCommentSetting();

    /**
     * 更新自动评价的评价设置。
     * 如果某个字段为null则不更新这个字段。
     *
     * @param autoCommentSettingDTO 自动评价的评价设置
     */
    void updateAutoCommentSetting(AutoCommentSettingDTO autoCommentSettingDTO);

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
    void updateAutoCloseCron(String cron) throws IllegalArgumentException, SchedulerException;

    /**
     * 初始化自动关闭订单定时任务
     * @throws SchedulerException 调度定时任务异常
     */
    void initAutoCloseJob() throws SchedulerException;

    /**
     * 暂停自动关闭订单定时任务
     * @throws SchedulerException 调度定时任务异常
     */
    void pauseAutoCloseJob() throws SchedulerException;

    /**
     * 恢复自动关闭订单定时任务
     * @throws SchedulerException 调度定时任务异常
     */
    void resumeAutoCloseJob() throws SchedulerException;

    /**
     * 删除自动关闭订单定时任务
     * @throws SchedulerException 调度定时任务异常
     */
    void deleteAutoCloseJob() throws SchedulerException;

    /**
     * 获取订单完成后经过多少时间自动关闭订单(单位秒)
     * @return 时间(单位秒)
     */
    Integer getAutoCloseTime();

    /**
     * 更新订单完成后经过多少时间自动关闭订单(单位秒)
     * @param time 时间(单位秒)
     * @throws IllegalArgumentException 时间小于0秒
     */
    void updateAutoCloseTime(Integer time) throws IllegalArgumentException;

    /**
     * 获取全部订单设置
     * @return 全部订单设置
     */
    OrderSettingDTO getAll();
}
