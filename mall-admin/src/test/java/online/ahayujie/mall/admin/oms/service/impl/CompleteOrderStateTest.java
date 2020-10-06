package online.ahayujie.mall.admin.oms.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.oms.bean.model.Order;
import online.ahayujie.mall.admin.oms.mapper.OrderMapper;
import online.ahayujie.mall.admin.oms.service.OrderContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class CompleteOrderStateTest {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private CompleteOrderState completeOrderState;

    @Test
    void closeOrder() {
        Order order = new Order();
        order.setMemberId(1L);
        order.setStatus(Order.Status.COMPLETE.getValue());
        orderMapper.insert(order);
        OrderContext orderContext = new OrderContext(null);
        completeOrderState.closeOrder(orderContext, order.getId());
        assertEquals(ClosedOrderState.class, orderContext.getOrderState().getClass());
        order = orderMapper.selectById(order.getId());
        assertEquals(Order.Status.CLOSED.getValue(), order.getStatus());
    }
}