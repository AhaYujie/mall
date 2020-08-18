package online.ahayujie.mall.admin.oms.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.oms.bean.model.Order;
import online.ahayujie.mall.admin.oms.service.AbstractOrderState;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * 申请退货退款订单状态 Service
 * @author aha
 * @since 2020/8/16
 */
@Slf4j
@Service(value = Order.APPLY_RETURN_STATUS_NAME)
public class ApplyReturnOrderState extends AbstractOrderState {
    public ApplyReturnOrderState(ApplicationContext applicationContext) {
        super(applicationContext);
    }
}
