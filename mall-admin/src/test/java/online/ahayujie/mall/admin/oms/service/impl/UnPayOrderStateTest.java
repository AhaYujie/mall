package online.ahayujie.mall.admin.oms.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.TestUtil;
import online.ahayujie.mall.admin.mms.bean.model.Member;
import online.ahayujie.mall.admin.mms.mapper.MemberMapper;
import online.ahayujie.mall.admin.oms.bean.model.Order;
import online.ahayujie.mall.admin.oms.bean.model.OrderProduct;
import online.ahayujie.mall.admin.oms.mapper.OrderMapper;
import online.ahayujie.mall.admin.oms.mapper.OrderProductMapper;
import online.ahayujie.mall.admin.oms.service.OrderContext;
import online.ahayujie.mall.admin.oms.service.OrderContextFactory;
import online.ahayujie.mall.admin.pms.bean.model.Sku;
import online.ahayujie.mall.admin.pms.mapper.SkuMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private OrderProductMapper orderProductMapper;
    @Autowired
    private SkuMapper skuMapper;

    @Test
    void cancelTimeoutOrder() {
    }

    @Test
    void memberCancelOrder() {
        Member member = new Member();
        member.setUsername(TestUtil.getRandomString(16));
        member.setPhone(TestUtil.getRandomNum(11));
        member.setIntegration(100);
        memberMapper.insert(member);
        Order order = new Order();
        order.setMemberId(member.getId());
        order.setUseIntegration(100);
        order.setStatus(Order.Status.UN_PAY.getValue());
        orderMapper.insert(order);
        order = orderMapper.selectById(order.getId());
        List<OrderProduct> orderProducts = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Sku sku = new Sku();
            sku.setStock(10);
            skuMapper.insert(sku);
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrderId(order.getId());
            orderProduct.setSkuId(sku.getId());
            orderProduct.setProductQuantity(i + 1);
            orderProducts.add(orderProduct);
        }
        orderProducts.forEach(orderProductMapper::insert);
        OrderContext orderContext = orderContextFactory.getOrderContext(order.getStatus());
        unPayOrderState.memberCancelOrder(orderContext, order.getId());
        assertEquals(orderContextFactory.getOrderState(Order.Status.CLOSED), orderContext.getOrderState());
        order = orderMapper.selectById(order.getId());
        assertEquals(Order.Status.CLOSED.getValue(), order.getStatus());
    }
}