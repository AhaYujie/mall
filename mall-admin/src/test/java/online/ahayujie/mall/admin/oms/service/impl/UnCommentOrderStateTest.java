package online.ahayujie.mall.admin.oms.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.mms.bean.model.Member;
import online.ahayujie.mall.admin.mms.mapper.MemberMapper;
import online.ahayujie.mall.admin.oms.bean.model.Order;
import online.ahayujie.mall.admin.oms.bean.model.OrderProduct;
import online.ahayujie.mall.admin.oms.mapper.OrderMapper;
import online.ahayujie.mall.admin.oms.mapper.OrderProductMapper;
import online.ahayujie.mall.admin.oms.service.OrderContext;
import online.ahayujie.mall.admin.oms.service.OrderContextFactory;
import online.ahayujie.mall.common.bean.model.Base;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class UnCommentOrderStateTest {
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderProductMapper orderProductMapper;
    @Autowired
    private UnCommentOrderState unCommentOrderState;
    @Autowired
    private OrderContextFactory orderContextFactory;

    @Test
    void comment() {
        Member member = new Member();
        member.setUsername(getRandomString(30));
        member.setPhone(getRandomNum(11));
        member.setNickname(getRandomString(30));
        memberMapper.insert(member);
        Order order = new Order();
        order.setMemberId(member.getId());
        order.setStatus(Order.Status.UN_COMMENT.getValue());
        orderMapper.insert(order);
        List<OrderProduct> orderProducts = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrderId(order.getId());
            orderProduct.setIsComment(OrderProduct.UN_COMMENT);
            orderProductMapper.insert(orderProduct);
            orderProducts.add(orderProduct);
        }
        List<Long> orderProductIds = orderProducts.stream().map(Base::getId).collect(Collectors.toList());

        OrderContext orderContext = orderContextFactory.getOrderContext(Order.Status.UN_COMMENT);
        // 评价订单部分商品
        List<Long> some = orderProductIds.subList(0, 5);
        unCommentOrderState.comment(orderContext, order.getId(), some, "content", null, 5);
        assertEquals(UnCommentOrderState.class, orderContext.getOrderState().getClass());
        order = orderMapper.selectById(order.getId());
        assertEquals(Order.Status.UN_COMMENT.getValue(), order.getStatus());
        // 评价完成订单全部商品
        List<Long> rest = orderProductIds.subList(5, 10);
        unCommentOrderState.comment(orderContext, order.getId(), rest, "content", null, 5);
        assertEquals(CompleteOrderState.class, orderContext.getOrderState().getClass());
        order = orderMapper.selectById(order.getId());
        assertEquals(Order.Status.COMPLETE.getValue(), order.getStatus());
    }

    private static String getRandomString(int length) {
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    private static String getRandomNum(int length) {
        String str = "0123456789";
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(random.nextInt(str.length()));
        }
        return stringBuilder.toString();
    }
}