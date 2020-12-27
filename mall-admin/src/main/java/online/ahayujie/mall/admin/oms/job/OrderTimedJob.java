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

    /**
     * 更新cron
     * @param cron cron
     * @throws IllegalArgumentException cron表达式不合法
     */
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

    public String getTriggerName() {
        return jobName;
    }

    public String getTriggerGroup() {
        return jobGroup;
    }

    public enum Status {
        /**
         * 不存在
         */
        NOT_EXIST(-1),

        /**
         * 正常
         */
        NORMAL(0),

        /**
         * 暂停
         */
        PAUSED(1)
        ;
        /**
         * 状态值
         */
        private final int value;

        Status(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    }
}
