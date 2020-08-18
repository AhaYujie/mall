package online.ahayujie.mall.admin.oms.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.oms.bean.model.Order;
import online.ahayujie.mall.admin.oms.service.AbstractOrderState;
import online.ahayujie.mall.admin.oms.service.OrderContext;
import online.ahayujie.mall.admin.oms.service.OrderState;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 交易关闭状态的订单 Service
 *
 * @author aha
 * @since 2020/8/15
 */
@Slf4j
@Service(value = Order.CLOSED_STATUS_NAME)
public class ClosedOrderState extends AbstractOrderState {

    public ClosedOrderState(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    @Override
    @PostConstruct
    protected void initOrderStateMap() {
        super.initOrderStateMap();
    }
}
