package online.ahayujie.mall.admin.oms.service;

import online.ahayujie.mall.admin.oms.job.OrderTimedJob;
import org.quartz.SchedulerException;

/**
 * 订单定时任务 Service
 *
 * @author aha
 * @since 2020/10/2
 */
public interface OrderTimedJobService {
    /**
     * 初始化所有定时任务
     * @throws SchedulerException SchedulerException
     */
    void initJobs() throws SchedulerException;

    /**
     * 初始化定时任务
     * @throws SchedulerException SchedulerException
     */
    void initJob(OrderTimedJob job) throws SchedulerException;

    /**
     * 更新定时任务
     * @param job 定时任务
     * @param cron 定时任务的cron
     * @throws SchedulerException SchedulerException
     */
    void updateJob(OrderTimedJob job, String cron) throws SchedulerException;

    /**
     * 暂停定时任务
     * @param job 定时任务
     * @throws SchedulerException SchedulerException
     */
    void pauseJob(OrderTimedJob job) throws SchedulerException;

    /**
     * 恢复运行定时任务
     * @param job 定时任务
     * @throws SchedulerException SchedulerException
     */
    void resumeJob(OrderTimedJob job) throws SchedulerException;

    /**
     * 删除定时任务
     * @param job 定时任务
     * @throws SchedulerException SchedulerException
     */
    void deleteJob(OrderTimedJob job) throws SchedulerException;
}
