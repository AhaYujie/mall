package online.ahayujie.mall.admin.oms.service.impl;

import online.ahayujie.mall.admin.bean.model.Dict;
import online.ahayujie.mall.admin.mapper.DictMapper;
import online.ahayujie.mall.admin.oms.bean.dto.OrderSettingDTO;
import online.ahayujie.mall.admin.oms.service.OrderSettingService;
import org.quartz.CronExpression;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author aha
 * @since 2020/8/12
 */
@Service
public class OrderSettingServiceImpl implements OrderSettingService {
    private static final String CODE = "order-setting";
    private static final String UN_PAY_TIMEOUT_KEY = "1";
    private static final String AUTO_CONFIRM_RECEIVE_TIME_KEY = "2";
    private static final String AUTO_COMMENT_TIME_KEY = "3";
    private static final String AUTO_CLOSE_TIME_KEY = "4";

    private final DictMapper dictMapper;

    public OrderSettingServiceImpl(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
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
        Dict dict = dictMapper.selectByCodeAndDictKey(CODE, AUTO_CONFIRM_RECEIVE_TIME_KEY);
        return dict.getDictValue();
    }

    @Override
    public void updateAutoConfirmReceiveCron(String cron) throws IllegalArgumentException {
        updateCronSetting(cron, AUTO_CONFIRM_RECEIVE_TIME_KEY);
    }

    @Override
    public String getAutoCommentCron() {
        Dict dict = dictMapper.selectByCodeAndDictKey(CODE, AUTO_COMMENT_TIME_KEY);
        return dict.getDictValue();
    }

    @Override
    public void updateAutoCommentCron(String cron) throws IllegalArgumentException {
        updateCronSetting(cron, AUTO_COMMENT_TIME_KEY);
    }

    @Override
    public String getAutoCloseCron() {
        Dict dict = dictMapper.selectByCodeAndDictKey(CODE, AUTO_CLOSE_TIME_KEY);
        return dict.getDictValue();
    }

    @Override
    public void updateAutoCloseCron(String cron) throws IllegalArgumentException {
        updateCronSetting(cron, AUTO_CLOSE_TIME_KEY);
    }

    @Override
    public OrderSettingDTO getAll() {
        List<Dict> dictList = dictMapper.selectByCode(CODE).stream()
                .filter(dict -> dict.getDictKey() != null)
                .collect(Collectors.toList());
        OrderSettingDTO orderSettingDTO = new OrderSettingDTO();
        for (Dict dict : dictList) {
            switch (dict.getDictKey()) {
                case UN_PAY_TIMEOUT_KEY:
                    orderSettingDTO.setUnPayTimeOut(Integer.valueOf(dict.getDictValue()));
                    break;
                case AUTO_CONFIRM_RECEIVE_TIME_KEY:
                    orderSettingDTO.setAutoConfirmReceiveCron(dict.getDictValue());
                    break;
                case AUTO_COMMENT_TIME_KEY:
                    orderSettingDTO.setAutoCommentCron(dict.getDictValue());
                    break;
                case AUTO_CLOSE_TIME_KEY:
                    orderSettingDTO.setAutoCloseCron(dict.getDictValue());
                    break;
            }
        }
        return orderSettingDTO;
    }

    private void updateCronSetting(String cron, String dictKey) {
        if (!CronExpression.isValidExpression(cron)) {
            throw new IllegalArgumentException("cron表达式不合法");
        }
        Dict dict = new Dict();
        dict.setCode(CODE);
        dict.setDictKey(dictKey);
        dict.setDictValue(cron);
        dict.setUpdateTime(new Date());
        dictMapper.updateByCodeAndDictKey(dict);
    }
}
