package online.ahayujie.mall.admin.oms.service.impl;

import online.ahayujie.mall.admin.bean.model.Dict;
import online.ahayujie.mall.admin.mapper.DictMapper;
import online.ahayujie.mall.admin.oms.bean.dto.OrderSettingDTO;
import online.ahayujie.mall.admin.oms.service.OrderSettingService;
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
    public Integer getAutoConfirmReceiveTime() {
        Dict dict = dictMapper.selectByCodeAndDictKey(CODE, AUTO_CONFIRM_RECEIVE_TIME_KEY);
        return Integer.valueOf(dict.getDictValue());
    }

    @Override
    public void updateAutoConfirmReceiveTime(Integer time) throws IllegalArgumentException {
        if (time <= 0) {
            throw new IllegalArgumentException("发货未确认收货超时自动确认时间小于等于0");
        }
        Dict dict = new Dict();
        dict.setCode(CODE);
        dict.setDictKey(AUTO_CONFIRM_RECEIVE_TIME_KEY);
        dict.setDictValue(time.toString());
        dict.setUpdateTime(new Date());
        dictMapper.updateByCodeAndDictKey(dict);
    }

    @Override
    public Integer getAutoCommentTime() {
        Dict dict = dictMapper.selectByCodeAndDictKey(CODE, AUTO_COMMENT_TIME_KEY);
        return Integer.valueOf(dict.getDictValue());
    }

    @Override
    public void updateAutoCommentTime(Integer time) throws IllegalArgumentException {
        if (time <= 0) {
            throw new IllegalArgumentException("确认收货后未评价超时自动评价时间小于等于0");
        }
        Dict dict = new Dict();
        dict.setCode(CODE);
        dict.setDictKey(AUTO_COMMENT_TIME_KEY);
        dict.setDictValue(time.toString());
        dict.setUpdateTime(new Date());
        dictMapper.updateByCodeAndDictKey(dict);
    }

    @Override
    public Integer getAutoCloseTime() {
        Dict dict = dictMapper.selectByCodeAndDictKey(CODE, AUTO_CLOSE_TIME_KEY);
        return Integer.valueOf(dict.getDictValue());
    }

    @Override
    public void updateAutoCloseTime(Integer time) throws IllegalArgumentException {
        if (time <= 0) {
            throw new IllegalArgumentException("订单交易完成后自动关闭交易，不能申请售后的时间小于等于0");
        }
        Dict dict = new Dict();
        dict.setCode(CODE);
        dict.setDictKey(AUTO_CLOSE_TIME_KEY);
        dict.setDictValue(time.toString());
        dict.setUpdateTime(new Date());
        dictMapper.updateByCodeAndDictKey(dict);
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
                    orderSettingDTO.setAutoConfirmReceiveTime(Integer.valueOf(dict.getDictValue()));
                    break;
                case AUTO_COMMENT_TIME_KEY:
                    orderSettingDTO.setAutoCommentTime(Integer.valueOf(dict.getDictValue()));
                    break;
                case AUTO_CLOSE_TIME_KEY:
                    orderSettingDTO.setAutoCloseTime(Integer.valueOf(dict.getDictValue()));
                    break;
            }
        }
        return orderSettingDTO;
    }
}
