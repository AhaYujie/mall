package online.ahayujie.mall.admin.oms.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.oms.bean.model.Order;
import online.ahayujie.mall.admin.oms.bean.model.OrderProduct;
import online.ahayujie.mall.admin.oms.mapper.OrderMapper;
import online.ahayujie.mall.admin.oms.mapper.OrderProductMapper;
import online.ahayujie.mall.admin.oms.service.OrderContext;
import online.ahayujie.mall.common.bean.model.Base;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class ApplyRefundOrderStateTest {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderProductMapper orderProductMapper;
    @Autowired
    private ApplyRefundOrderState applyRefundOrderState;

    @Test
    void refuseAfterSaleApply() {
        Order order = new Order();
        order.setMemberId(1L);
        order.setStatus(Order.Status.APPLY_REFUND.getValue());
        orderMapper.insert(order);
        List<OrderProduct> orderProducts = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrderId(order.getId());
            orderProduct.setStatus(OrderProduct.Status.AFTER_SALE.getValue());
            orderProducts.add(orderProduct);
        }
        orderProducts.forEach(orderProductMapper::insert);
        List<Long> ids = orderProducts.stream().map(Base::getId).collect(Collectors.toList());
        OrderContext orderContext = new OrderContext(null);
        applyRefundOrderState.refuseAfterSaleApply(orderContext, order.getId(), ids);
        assertEquals(CompleteOrderState.class, orderContext.getOrderState().getClass());
        order = orderMapper.selectById(order.getId());
        assertEquals(Order.Status.COMPLETE.getValue(), order.getStatus());
        List<OrderProduct> orderProducts1 = ids.stream().map(id -> orderProductMapper.selectById(id)).collect(Collectors.toList());
        for (OrderProduct orderProduct : orderProducts1) {
            assertEquals(OrderProduct.Status.PAY.getValue(), orderProduct.getStatus());
        }
    }

    @Test
    void agreeAfterSaleApply() {
        Order order = new Order();
        order.setMemberId(1L);
        order.setStatus(Order.Status.APPLY_REFUND.getValue());
        orderMapper.insert(order);
        OrderContext orderContext = new OrderContext(null);
        applyRefundOrderState.agreeAfterSaleApply(orderContext, order.getId());
        assertEquals(RefundOrderState.class, orderContext.getOrderState().getClass());
        order = orderMapper.selectById(order.getId());
        assertEquals(Order.Status.REFUND.getValue(), order.getStatus());
    }
}