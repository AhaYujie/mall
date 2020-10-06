package online.ahayujie.mall.admin.oms.job;

import online.ahayujie.mall.admin.bean.model.Dict;
import online.ahayujie.mall.admin.mapper.DictMapper;
import org.quartz.CronExpression;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

/**
 * 订单定时任务
 *
 * @author aha
 * @since 2020/10/2
 */
public abstract class OrderTimedJob extends QuartzJobBean {
    protected final static String DICT_CODE = "order-setting";

    protected String cronDictKey;
    protected String jobName;
    protected String jobGroup;

    protected final DictMapper dictMapper;

    public OrderTimedJob(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    public String getCron() {
        Dict dict = dictMapper.selectByCodeAndDictKey(DICT_CODE, cronDictKey);
        return dict.getDictValue();
    }

    public void updateCron(String cron) {
        if (!CronExpression.isValidExpression(cron)) {
            throw new IllegalArgumentException("cron表达式不合法");
        }
        Dict dict = new Dict();
        dict.setCode(DICT_CODE);
        dict.setDictKey(cronDictKey);
        dict.setDictValue(cron);
        dict.setUpdateTime(new Date());
        dictMapper.updateByCodeAndDictKey(dict);
    }

    public String getJobName() {
        return jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }
}
