package online.ahayujie.mall.admin.oms.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.bean.model.Dict;
import online.ahayujie.mall.admin.mapper.DictMapper;
import online.ahayujie.mall.admin.oms.bean.dto.AutoCommentSettingDTO;
import online.ahayujie.mall.admin.oms.bean.dto.OrderSettingDTO;
import online.ahayujie.mall.admin.oms.bean.dto.OrderTimedJobStatusDTO;
import online.ahayujie.mall.admin.oms.job.AutoCloseJob;
import online.ahayujie.mall.admin.oms.job.AutoCommentJob;
import online.ahayujie.mall.admin.oms.job.AutoConfirmReceiveJob;
import online.ahayujie.mall.admin.oms.job.OrderTimedJob;
import online.ahayujie.mall.admin.oms.service.OrderSettingService;
import online.ahayujie.mall.admin.oms.service.OrderTimedJobService;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author aha
 * @since 2020/8/12
 */
@Slf4j
@Service
public class OrderSettingServiceImpl implements OrderSettingService {
    private static final String CODE = "order-setting";
    private static final String UN_PAY_TIMEOUT_KEY = "1";

    private final DictMapper dictMapper;
    private final List<OrderTimedJob> orderTimedJobs;
    private final OrderTimedJobService orderTimedJobService;

    public OrderSettingServiceImpl(DictMapper dictMapper, List<OrderTimedJob> orderTimedJobs, OrderTimedJobService orderTimedJobService) {
        this.dictMapper = dictMapper;
        this.orderTimedJobs = orderTimedJobs;
        this.orderTimedJobService = orderTimedJobService;
    }

    @Override
    public Integer getUnPayTimeout() {
        Dict dict = dictMapper.selectByCodeAndDictKey(CODE, UN_PAY_TIMEOUT_KEY);
        return Integer.valueOf(dict.getDictValue());
    }

    @Override
    public void updateUnPayTimeOut(Integer time) throws IllegalArgumentException {
        if (time <= 0) {
            throw new IllegalArgumentException("订单未支付超时关闭的时间小于等于0");
        }
        Dict dict = new Dict();
        dict.setCode(CODE);
        dict.setDictKey(UN_PAY_TIMEOUT_KEY);
        dict.setDictValue(time.toString());
        dict.setUpdateTime(new Date());
        dictMapper.updateByCodeAndDictKey(dict);
    }

    @Override
    public String getAutoConfirmReceiveCron() {
        return getJob(AutoConfirmReceiveJob.class).getCron();
    }

    @Override
    public Integer getAutoConfirmReceiveTime() {
        AutoConfirmReceiveJob job = (AutoConfirmReceiveJob) getJob(AutoConfirmReceiveJob.class);
        return job.getAutoConfirmReceiveTime();
    }

    @Override
    public void updateAutoConfirmReceiveTime(Integer time) throws IllegalArgumentException {
        AutoConfirmReceiveJob job = (AutoConfirmReceiveJob) getJob(AutoConfirmReceiveJob.class);
        job.updateAutoConfirmReceiveTime(time);
    }

    @Override
    public String getAutoCommentCron() {
        return getJob(AutoCommentJob.class).getCron();
    }

    @Override
    public Integer getAutoCommentTime() {
        AutoCommentJob job = (AutoCommentJob) getJob(AutoCommentJob.class);
        return job.getAutoCommentTime();
    }

    @Override
    public void updateAutoCommentTime(Integer time) throws IllegalArgumentException {
        AutoCommentJob job = (AutoCommentJob) getJob(AutoCommentJob.class);
        job.updateAutoCommentTime(time);
    }

    @Override
    public AutoCommentSettingDTO getAutoCommentSetting() {
        AutoCommentJob job = (AutoCommentJob) getJob(AutoCommentJob.class);
        AutoCommentSettingDTO autoCommentSettingDTO = new AutoCommentSettingDTO();
        autoCommentSettingDTO.setContent(job.getAutoCommentContent());
        autoCommentSettingDTO.setPics(job.getAutoCommentPics());
        autoCommentSettingDTO.setStar(job.getAutoCommentStar());
        return autoCommentSettingDTO;
    }

    @Override
    public void updateAutoCommentSetting(AutoCommentSettingDTO autoCommentSettingDTO) {
        AutoCommentJob job = (AutoCommentJob) getJob(AutoCommentJob.class);
        if (autoCommentSettingDTO.getContent() != null) {
            job.updateAutoCommentContent(autoCommentSettingDTO.getContent());
        }
        if (autoCommentSettingDTO.getPics() != null) {
            job.updateAutoCommentPics(autoCommentSettingDTO.getPics());
        }
        if (autoCommentSettingDTO.getStar() != null) {
            job.updateAutoCommentStar(autoCommentSettingDTO.getStar());
        }
    }

    @Override
    public String getAutoCloseCron() {
        return getJob(AutoCloseJob.class).getCron();
    }

    @Override
    public Integer getAutoCloseTime() {
        AutoCloseJob job = (AutoCloseJob) getJob(AutoCloseJob.class);
        return job.getAutoCloseTime();
    }

    @Override
    public void updateAutoCloseTime(Integer time) throws IllegalArgumentException {
        AutoCloseJob job = (AutoCloseJob) getJob(AutoCloseJob.class);
        job.updateAutoCloseTime(time);
    }

    @Override
    public OrderSettingDTO getAll() {
        OrderSettingDTO orderSettingDTO = new OrderSettingDTO();
        orderSettingDTO.setUnPayTimeOut(getUnPayTimeout());
        orderSettingDTO.setAutoConfirmReceiveCron(getAutoConfirmReceiveCron());
        orderSettingDTO.setAutoCommentCron(getAutoCommentCron());
        orderSettingDTO.setAutoCloseCron(getAutoCloseCron());
        return orderSettingDTO;
    }

    @Override
    public OrderTimedJobStatusDTO getTimedJobStatus(Integer job) throws SchedulerException {
        OrderTimedJobStatusDTO orderTimedJobStatusDTO = new OrderTimedJobStatusDTO();
        OrderTimedJob orderTimedJob = getJob(job);
        if (orderTimedJob == null) {
            orderTimedJobStatusDTO.setStatus(OrderTimedJob.Status.NOT_EXIST.value());
        } else {
            try {
                orderTimedJobStatusDTO.setStatus(orderTimedJobService.getJobState(orderTimedJob).value());
            } catch (SchedulerException e) {
                log.error(e.getMessage());
                e.printStackTrace();
                throw e;
            }
        }
        return orderTimedJobStatusDTO;
    }

    @Override
    public void initTimedJob(Integer job) throws SchedulerException {
        operateTimedJob(job, 0);
    }

    @Override
    public void pauseTimedJob(Integer job) throws SchedulerException {
        operateTimedJob(job, 1);
    }

    @Override
    public void resumeTimedJob(Integer job) throws SchedulerException {
        operateTimedJob(job, 2);
    }

    @Override
    public void deleteTimedJob(Integer job) throws SchedulerException {
        operateTimedJob(job, 3);
    }

    @Override
    public String getTimedJobCron(Integer job) {
        OrderTimedJob orderTimedJob = getJob(job);
        return (orderTimedJob == null) ? null : orderTimedJob.getCron();
    }

    @Override
    public void updateTimedJobCron(Integer job, String cron) throws SchedulerException {
        OrderTimedJob orderTimedJob = getJob(job);
        if (orderTimedJob == null) {
            return;
        }
        orderTimedJob.updateCron(cron);
        try {
            orderTimedJobService.updateJob(orderTimedJob, cron);
        } catch (SchedulerException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 操作订单定时任务.
     * 如果定时任务不存在或操作不存在则不做直接返回.
     *
     * @param job 0->自动确认收货; 1->自动评价; 2->自动关闭订单
     * @param operation 0->初始化; 1->暂停; 2->恢复; 3->删除
     * @throws SchedulerException 调度定时任务异常
     */
    private void operateTimedJob(Integer job, Integer operation) throws SchedulerException {
        OrderTimedJob orderTimedJob = getJob(job);
        if (orderTimedJob == null) {
            return;
        }
        try {
            switch (operation) {
                case 0:
                    orderTimedJobService.initJob(orderTimedJob);
                    break;
                case 1:
                    orderTimedJobService.pauseJob(orderTimedJob);
                    break;
                case 2:
                    orderTimedJobService.resumeJob(orderTimedJob);
                    break;
                case 3:
                    orderTimedJobService.deleteJob(orderTimedJob);
                    break;
            }
        } catch (SchedulerException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private OrderTimedJob getJob(Class<? extends OrderTimedJob> jobClass) {
        for (OrderTimedJob job : orderTimedJobs) {
            if (job.getClass().equals(jobClass)) {
                return job;
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * 获取 {@link OrderTimedJob} 单例.
     * 如果不存在则返回null.
     *
     * @param job 0->自动确认收货; 1->自动评价; 2->自动关闭订单
     * @return {@link OrderTimedJob} 单例
     */
    private OrderTimedJob getJob(Integer job) {
        switch (job) {
            case 0:
                return getJob(AutoConfirmReceiveJob.class);
            case 1:
                return getJob(AutoCommentJob.class);
            case 2:
                return getJob(AutoCloseJob.class);
        }
        return null;
    }

    private void updateCron(Class<? extends OrderTimedJob> jobClass, String cron) throws SchedulerException {
        OrderTimedJob job = getJob(jobClass);
        job.updateCron(cron);
        try {
            orderTimedJobService.updateJob(job, cron);
        } catch (SchedulerException e) {
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
            throw e;
        }
    }
}
