package online.ahayujie.mall.admin.oms.service;

import online.ahayujie.mall.admin.oms.bean.dto.AutoCommentSettingDTO;
import online.ahayujie.mall.admin.oms.bean.dto.OrderSettingDTO;
import online.ahayujie.mall.admin.oms.bean.dto.OrderTimedJobStatusDTO;
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

    /**
     * 获取订单定时任务状态
     * @param job 0->自动确认收货; 1->自动评价; 2->自动关闭订单
     * @return 订单定时任务状态
     * @throws SchedulerException 调度定时任务异常
     */
    OrderTimedJobStatusDTO getTimedJobStatus(Integer job) throws SchedulerException;

    /**
     * 初始化订单定时任务
     * @param job 0->自动确认收货; 1->自动评价; 2->自动关闭订单
     * @throws SchedulerException 调度定时任务异常
     */
    void initTimedJob(Integer job) throws SchedulerException;

    /**
     * 暂停订单定时任务
     * @param job 0->自动确认收货; 1->自动评价; 2->自动关闭订单
     * @throws SchedulerException 调度定时任务异常
     */
    void pauseTimedJob(Integer job) throws SchedulerException;

    /**
     * 恢复订单定时任务
     * @param job 0->自动确认收货; 1->自动评价; 2->自动关闭订单
     * @throws SchedulerException 调度定时任务异常
     */
    void resumeTimedJob(Integer job) throws SchedulerException;

    /**
     * 删除订单定时任务
     * @param job 0->自动确认收货; 1->自动评价; 2->自动关闭订单
     * @throws SchedulerException 调度定时任务异常
     */
    void deleteTimedJob(Integer job) throws SchedulerException;

    /**
     * 获取订单定时任务的cron.
     * 如果定时任务不存在则返回null.
     *
     * @param job 0->自动确认收货; 1->自动评价; 2->自动关闭订单
     * @return 订单定时任务的cron
     */
    String getTimedJobCron(Integer job);

    /**
     * 更新定时任务的cron
     * @param job 0->自动确认收货; 1->自动评价; 2->自动关闭订单
     * @param cron cron表达式
     * @throws SchedulerException 调度定时任务异常
     * @throws IllegalArgumentException cron表达式不合法
     */
    void updateTimedJobCron(Integer job, String cron) throws SchedulerException;
}
