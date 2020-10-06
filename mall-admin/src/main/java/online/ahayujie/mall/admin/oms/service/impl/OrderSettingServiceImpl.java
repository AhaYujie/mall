package online.ahayujie.mall.admin.oms.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.bean.model.Dict;
import online.ahayujie.mall.admin.mapper.DictMapper;
import online.ahayujie.mall.admin.oms.bean.dto.AutoCommentSettingDTO;
import online.ahayujie.mall.admin.oms.bean.dto.OrderSettingDTO;
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
    public void updateAutoConfirmReceiveCron(String cron) throws IllegalArgumentException, SchedulerException {
        updateCron(AutoConfirmReceiveJob.class, cron);
    }

    @Override
    public void initAutoConfirmReceiveJob() throws SchedulerException {
        orderTimedJobService.initJob(getJob(AutoConfirmReceiveJob.class));
    }

    @Override
    public void pauseAutoConfirmReceiveJob() throws SchedulerException {
        orderTimedJobService.pauseJob(getJob(AutoConfirmReceiveJob.class));
    }

    @Override
    public void resumeAutoConfirmReceiveJob() throws SchedulerException {
        orderTimedJobService.resumeJob(getJob(AutoConfirmReceiveJob.class));
    }

    @Override
    public void deleteAutoConfirmReceiveJob() throws SchedulerException {
        orderTimedJobService.deleteJob(getJob(AutoConfirmReceiveJob.class));
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
    public void updateAutoCommentCron(String cron) throws IllegalArgumentException, SchedulerException {
        updateCron(AutoCommentJob.class, cron);
    }

    @Override
    public void initAutoCommentJob() throws SchedulerException {
        orderTimedJobService.initJob(getJob(AutoCommentJob.class));
    }

    @Override
    public void pauseAutoCommentJob() throws SchedulerException {
        orderTimedJobService.pauseJob(getJob(AutoCommentJob.class));
    }

    @Override
    public void resumeAutoCommentJob() throws SchedulerException {
        orderTimedJobService.resumeJob(getJob(AutoCommentJob.class));
    }

    @Override
    public void deleteAutoCommentJob() throws SchedulerException {
        orderTimedJobService.deleteJob(getJob(AutoCommentJob.class));
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
    public void updateAutoCloseCron(String cron) throws IllegalArgumentException, SchedulerException {
        updateCron(AutoCloseJob.class, cron);
    }

    @Override
    public void initAutoCloseJob() throws SchedulerException {
        orderTimedJobService.initJob(getJob(AutoCloseJob.class));
    }

    @Override
    public void pauseAutoCloseJob() throws SchedulerException {
        orderTimedJobService.pauseJob(getJob(AutoCloseJob.class));
    }

    @Override
    public void resumeAutoCloseJob() throws SchedulerException {
        orderTimedJobService.resumeJob(getJob(AutoCloseJob.class));
    }

    @Override
    public void deleteAutoCloseJob() throws SchedulerException {
        orderTimedJobService.deleteJob(getJob(AutoCloseJob.class));
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

    private OrderTimedJob  getJob(Class<? extends OrderTimedJob> jobClass) {
        for (OrderTimedJob job : orderTimedJobs) {
            if (job.getClass().equals(jobClass)) {
                return job;
            }
        }
        throw new IllegalArgumentException();
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
