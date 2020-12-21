package online.ahayujie.mall.portal.oms.service.impl;

import online.ahayujie.mall.portal.bean.model.Dict;
import online.ahayujie.mall.portal.mapper.DictMapper;
import online.ahayujie.mall.portal.oms.service.OrderSettingService;
import org.springframework.stereotype.Service;

/**
 * @author aha
 * @since 2020/12/18
 */
@Service
public class OrderSettingServiceImpl implements OrderSettingService {
    private static final String CODE = "order-setting";
    private static final String UN_PAY_TIMEOUT_KEY = "1";

    private final DictMapper dictMapper;

    public OrderSettingServiceImpl(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    @Override
    public Integer getUnPayTimeout() {
        Dict dict = dictMapper.selectByCodeAndDictKey(CODE, UN_PAY_TIMEOUT_KEY);
        return Integer.valueOf(dict.getDictValue());
    }
}
