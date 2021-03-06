package online.ahayujie.mall.admin.oms.job;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.bean.model.Dict;
import online.ahayujie.mall.admin.mapper.DictMapper;
import online.ahayujie.mall.admin.oms.bean.model.Order;
import online.ahayujie.mall.admin.oms.mapper.OrderMapper;
import online.ahayujie.mall.admin.oms.service.OrderService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author aha
 * @since 2020/10/2
 */
@Slf4j
@Service
public class AutoConfirmReceiveJob extends OrderTimedJob {
    private static final String TIME_DICT_KEY = "5";

    private OrderService orderService;

    public AutoConfirmReceiveJob(DictMapper dictMapper) {
        super(dictMapper);
        this.cronDictKey = "2";
        this.jobName = "auto-confirm-receive";
        this.jobGroup = "order-timed-job";
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        long seconds = getAutoConfirmReceiveTime();
        Date now = new Date();
        Date time = new Date(now.getTime() - seconds * 1000);
        List<Long> orderIds = orderService.getOrderNeedToAutoConfirmReceive(time);
        for (Long orderId : orderIds) {
            orderService.confirmReceive(orderId);
        }
    }

    /**
     * 获取发货后经过多少时间未确认收货则自动确认收货(单位秒)
     * @return 时间(单位秒)
     */
    public Integer getAutoConfirmReceiveTime() {
        Dict dict = dictMapper.selectByCodeAndDictKey(DICT_CODE, TIME_DICT_KEY);
        return Integer.valueOf(dict.getDictValue());
    }

    /**
     * 更新发货后经过多少时间未确认收货则自动确认收货(单位秒)
     * @param time 时间(单位秒)
     * @throws IllegalArgumentException 时间小于0秒
     */
    public void updateAutoConfirmReceiveTime(Integer time) throws IllegalArgumentException {
        if (time < 0) {
            throw new IllegalArgumentException("时间小于0秒");
        }
        Dict dict = new Dict();
        dict.setCode(DICT_CODE);
        dict.setUpdateTime(new Date());
        dict.setDictKey(TIME_DICT_KEY);
        dict.setDictValue(time.toString());
        dictMapper.updateByCodeAndDictKey(dict);
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }
}
