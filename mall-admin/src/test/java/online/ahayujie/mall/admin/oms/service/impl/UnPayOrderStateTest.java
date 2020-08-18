package online.ahayujie.mall.admin.oms.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.oms.bean.model.Order;
import online.ahayujie.mall.admin.oms.mapper.OrderMapper;
import online.ahayujie.mall.admin.oms.service.OrderContext;
import online.ahayujie.mall.admin.oms.service.OrderContextFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class UnPayOrderStateTest {
    @Autowired
    private UnPayOrderState unPayOrderState;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderContextFactory orderContextFactory;

    @Test
    void cancelTimeoutOrder() {
    }

    @Test
    void memberCancelOrder() {
        Order order = new Order();
        order.setMemberId(1L);
        orderMapper.insert(order);
        order = orderMapper.selectById(order.getId());
        OrderContext orderContext = orderContextFactory.getOrderContext(order.getStatus());
        unPayOrderState.memberCancelOrder(orderContext, order.getId());
        assertEquals(orderContextFactory.getOrderState(Order.Status.CLOSED), orderContext.getOrderState());
        order = orderMapper.selectById(order.getId());
        assertEquals(Order.Status.CLOSED.getValue(), order.getStatus());
    }
}