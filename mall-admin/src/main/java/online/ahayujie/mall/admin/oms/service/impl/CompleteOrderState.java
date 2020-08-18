package online.ahayujie.mall.admin.oms.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.oms.bean.model.Order;
import online.ahayujie.mall.admin.oms.service.AbstractOrderState;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * 交易完成订单状态 Service
 * @author aha
 * @since 2020/8/16
 */
@Slf4j
@Service(value = Order.COMPLETE_STATUS_NAME)
public class CompleteOrderState extends AbstractOrderState {
    public CompleteOrderState(ApplicationContext applicationContext) {
        super(applicationContext);
    }
}
