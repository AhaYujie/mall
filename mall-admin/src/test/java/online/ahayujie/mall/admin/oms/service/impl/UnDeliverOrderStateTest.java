package online.ahayujie.mall.admin.oms.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.oms.bean.dto.DeliverOrderParam;
import online.ahayujie.mall.admin.oms.bean.model.Order;
import online.ahayujie.mall.admin.oms.mapper.OrderMapper;
import online.ahayujie.mall.admin.oms.service.OrderContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class UnDeliverOrderStateTest {
    @Autowired
    private UnDeliverOrderState unDeliverOrderState;
    @Autowired
    private OrderMapper orderMapper;

    @Test
    void deliverOrder() {
        Order order = new Order();
        order.setMemberId(1L);
        order.setStatus(Order.Status.UN_DELIVER.getValue());
        orderMapper.insert(order);
        DeliverOrderParam param = new DeliverOrderParam();
        param.setId(order.getId());
        param.setDeliverySn("1234567");
        param.setDeliveryCompany("东风快递");
        param.setDeliveryTime(new Date());
        OrderContext orderContext = new OrderContext(null);
        unDeliverOrderState.deliverOrder(orderContext, param);
        assertEquals(DeliveredOrderState.class, orderContext.getOrderState().getClass());
        order = orderMapper.selectById(order.getId());
        assertEquals(param.getDeliverySn(), order.getDeliverySn());
        assertEquals(param.getDeliveryCompany(), order.getDeliveryCompany());
        assertEquals(Order.Status.DELIVERED.getValue(), order.getStatus());
    }
}