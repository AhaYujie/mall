package online.ahayujie.mall.admin.oms.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.oms.job.OrderTimedJob;
import online.ahayujie.mall.admin.oms.service.OrderTimedJobService;
import org.quartz.*;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author aha
 * @since 2020/10/2
 */
@Slf4j
@Service
public class OrderTimedJobServiceImpl implements OrderTimedJobService {
    private final Scheduler scheduler;
    private final List<OrderTimedJob> jobs;

    public OrderTimedJobServiceImpl(Scheduler scheduler, List<OrderTimedJob> jobs) {
        this.scheduler = scheduler;
        this.jobs = jobs;
    }

    @Override
    @EventListener(classes = ContextRefreshedEvent.class)
    public void initJobs() throws SchedulerException {
        log.debug("初始化全部订单定时任务");
        for (OrderTimedJob job : jobs) {
            initJob(job);
        }
    }

    @Override
    public void initJob(OrderTimedJob job) throws SchedulerException {
        JobKey jobKey = new JobKey(job.getJobName(), job.getJobGroup());
        if (scheduler.checkExists(jobKey)) {
            log.debug("{} 定时任务已经存在，无需初始化", job.getJobName());
            return;
        }
        JobDetail jobDetail = JobBuilder.newJob(job.getClass())
                .withIdentity(jobKey)
                .storeDurably()
                .build();
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCron()).withMisfireHandlingInstructionDoNothing();
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(job.getJobName(), job.getJobGroup())
                .startAt(new Date())
                .withSchedule(scheduleBuilder)
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
        log.debug("{} 定时任务初始化完成", job.getJobName());
    }

    @Override
    public void updateJob(OrderTimedJob job, String cron) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
        if (scheduler.checkExists(triggerKey)) {
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();
            scheduler.rescheduleJob(triggerKey, trigger);
        }
    }

    @Override
    public void pauseJob(OrderTimedJob job) throws SchedulerException {
        JobKey jobKey = new JobKey(job.getJobName(), job.getJobGroup());
        scheduler.pauseJob(jobKey);
    }

    @Override
    public void resumeJob(OrderTimedJob job) throws SchedulerException {
        JobKey jobKey = new JobKey(job.getJobName(), job.getJobGroup());
        scheduler.resumeJob(jobKey);
    }

    @Override
    public void deleteJob(OrderTimedJob job) throws SchedulerException {
        JobKey jobKey = new JobKey(job.getJobName(), job.getJobGroup());
        scheduler.deleteJob(jobKey);
    }
}
