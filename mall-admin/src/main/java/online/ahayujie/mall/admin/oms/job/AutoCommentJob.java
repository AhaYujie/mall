package online.ahayujie.mall.admin.oms.job;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.bean.model.Dict;
import online.ahayujie.mall.admin.mapper.DictMapper;
import online.ahayujie.mall.admin.oms.service.OrderService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author aha
 * @since 2020/10/2
 */
@Slf4j
@Service
public class AutoCommentJob extends OrderTimedJob {
    private static final String TIME_DICT_KEY = "6";
    private static final String CONTENT_DICT_KEY = "8";
    private static final String PICS_DICT_KEY = "9";
    private static final String STAR_DICT_KEY = "10";

    private OrderService orderService;

    public AutoCommentJob(DictMapper dictMapper) {
        super(dictMapper);
        this.cronDictKey = "3";
        this.jobName = "auto-comment";
        this.jobGroup = "order-timed-job";
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        long seconds = getAutoCommentTime();
        Date now = new Date();
        Date time = new Date(now.getTime() - seconds * 1000);
        List<Long> orderIds = orderService.getOrderNeedToAutoComment(time);
        String content = getAutoCommentContent();
        String pic = getAutoCommentPics();
        Integer star = getAutoCommentStar();
        for (Long orderId : orderIds) {
            List<Long> orderProductIds = orderService.getUnCommentOrderProduct(orderId);
            orderService.comment(orderId, orderProductIds, content, pic, star);
        }
    }

    /**
     * 获取确认收货后经过多少时间未评价则自动评价(单位秒)
     * @return 时间(单位秒)
     */
    public Integer getAutoCommentTime() {
        Dict dict = dictMapper.selectByCodeAndDictKey(DICT_CODE, TIME_DICT_KEY);
        return Integer.valueOf(dict.getDictValue());
    }

    /**
     * 更新确认收货后经过多少时间未评价则自动评价(单位秒)
     * @param time 时间(单位秒)
     * @throws IllegalArgumentException 时间小于0秒
     */
    public void updateAutoCommentTime(Integer time) throws IllegalArgumentException {
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

    /**
     * 获取自动评价的评价内容
     * @return 评价内容
     */
    public String getAutoCommentContent() {
        return dictMapper.selectByCodeAndDictKey(DICT_CODE, CONTENT_DICT_KEY).getDictValue();
    }

    /**
     * 更新自动评价的评价内容
     * @param content 评价内容
     */
    public void updateAutoCommentContent(String content) {
        Dict dict = new Dict();
        dict.setCode(DICT_CODE);
        dict.setUpdateTime(new Date());
        dict.setDictKey(CONTENT_DICT_KEY);
        dict.setDictValue(content);
        dictMapper.updateByCodeAndDictKey(dict);
    }

    /**
     * 获取自动评价的评价图片
     * @return 评价图片
     */
    public String getAutoCommentPics() {
        return dictMapper.selectByCodeAndDictKey(DICT_CODE, PICS_DICT_KEY).getDictValue();
    }

    /**
     * 更新自动评价的评价图片
     * @param pics 评价图片
     */
    public void updateAutoCommentPics(String pics) {
        Dict dict = new Dict();
        dict.setCode(DICT_CODE);
        dict.setUpdateTime(new Date());
        dict.setDictKey(PICS_DICT_KEY);
        dict.setDictValue(pics);
        dictMapper.updateByCodeAndDictKey(dict);
    }

    /**
     * 获取自动评价的评价星数
     * @return 评价星数
     */
    public Integer getAutoCommentStar() {
        return Integer.valueOf(dictMapper.selectByCodeAndDictKey(DICT_CODE, STAR_DICT_KEY).getDictValue());
    }

    /**
     * 更新自动评价的评价星数
     * @param star 评价星数
     */
    public void updateAutoCommentStar(Integer star) {
        Dict dict = new Dict();
        dict.setCode(DICT_CODE);
        dict.setUpdateTime(new Date());
        dict.setDictKey(STAR_DICT_KEY);
        dict.setDictValue(star.toString());
        dictMapper.updateByCodeAndDictKey(dict);
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }
}
