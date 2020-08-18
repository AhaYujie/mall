package online.ahayujie.mall.admin.oms.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.oms.bean.dto.OrderDetailDTO;
import online.ahayujie.mall.admin.oms.bean.dto.OrderListDTO;
import online.ahayujie.mall.admin.oms.bean.dto.QueryOrderListParam;
import online.ahayujie.mall.admin.oms.bean.model.Order;
import online.ahayujie.mall.admin.oms.bean.model.OrderProduct;
import online.ahayujie.mall.admin.oms.exception.IllegalOrderException;
import online.ahayujie.mall.admin.oms.mapper.OrderMapper;
import online.ahayujie.mall.admin.oms.mapper.OrderProductMapper;
import online.ahayujie.mall.common.api.CommonPage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderProductMapper orderProductMapper;

    @Test
    void list() {
        // not exist
        QueryOrderListParam param = new QueryOrderListParam();
        param.setOrderSn("不存在的订单号");
        CommonPage<OrderListDTO> result = orderService.list(1, 5, param);
        assertEquals(0, result.getData().size());
        QueryOrderListParam param1 = new QueryOrderListParam();
        param1.setStatus(-1);
        CommonPage<OrderListDTO> result1 = orderService.list(1, 5, param1);
        assertEquals(0, result1.getData().size());
        QueryOrderListParam param2 = new QueryOrderListParam();
        param2.setOrderType(-1);
        CommonPage<OrderListDTO> result2 = orderService.list(1, 5, param2);
        assertEquals(0, result2.getData().size());
        QueryOrderListParam param3 = new QueryOrderListParam();
        param3.setSourceType(-1);
        CommonPage<OrderListDTO> result3 = orderService.list(1, 5, param3);
        assertEquals(0, result3.getData().size());
        QueryOrderListParam param4 = new QueryOrderListParam();
        param4.setMemberUsername("不存在的用户名123456abcdefg");
        CommonPage<OrderListDTO> result4 = orderService.list(1, 5, param4);
        assertEquals(0, result4.getData().size());

        // exist
        List<Order> orders = orderMapper.selectList(Wrappers.emptyWrapper());
        orders.forEach(order -> orderMapper.deleteById(order.getId()));

        List<Order> orders1 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Order order = new Order();
            order.setMemberId(1L);
            order.setOrderSn("for test: " + i);
            orders1.add(order);
        }
        Order order = new Order();
        order.setMemberId(1L);
        order.setOrderSn("for test: query");
        order.setStatus(Order.Status.CLOSED.getValue());
        order.setOrderType(Order.Type.MIAO_SHA.getValue());
        order.setSourceType(Order.SourceType.APP.getValue());
        order.setMemberUsername("for test: member username");
        orders1.add(order);
        orders1.forEach(orderMapper::insert);
        CommonPage<OrderListDTO> result5 = orderService.list(1, 5, new QueryOrderListParam());
        assertEquals(5, result5.getData().size());
        CommonPage<OrderListDTO> result6 = orderService.list(3, 5, new QueryOrderListParam());
        assertEquals(1, result6.getData().size());

        QueryOrderListParam param5 = new QueryOrderListParam();
        param5.setOrderSn(order.getOrderSn());
        CommonPage<OrderListDTO> result11 = orderService.list(1, 5, param5);
        assertEquals(1, result11.getData().size());
        QueryOrderListParam param6 = new QueryOrderListParam();
        param6.setStatus(order.getStatus());
        CommonPage<OrderListDTO> result12 = orderService.list(1, 5, param6);
        assertEquals(1, result12.getData().size());
        QueryOrderListParam param7 = new QueryOrderListParam();
        param7.setOrderType(order.getOrderType());
        CommonPage<OrderListDTO> result13 = orderService.list(1, 5, param7);
        assertEquals(1, result13.getData().size());
        QueryOrderListParam param8 = new QueryOrderListParam();
        param8.setSourceType(order.getSourceType());
        CommonPage<OrderListDTO> result14 = orderService.list(1, 5, param8);
        assertEquals(1, result14.getData().size());
        QueryOrderListParam param9 = new QueryOrderListParam();
        param9.setMemberUsername(order.getMemberUsername());
        CommonPage<OrderListDTO> result15 = orderService.list(1, 5, param9);
        assertEquals(1, result15.getData().size());
    }

    @Test
    void getOrderDetail() {
        // not exist
        Throwable throwable = assertThrows(IllegalOrderException.class, () -> orderService.getOrderDetail(-1L));
        log.debug(throwable.getMessage());

        // exist
        Order order = new Order();
        order.setMemberId(1L);
        order.setOrderSn("for test");
        orderMapper.insert(order);
        order = orderMapper.selectById(order.getId());
        List<OrderProduct> orderProducts = new ArrayList<>();
        for (int i = 0; i < new Random().nextInt(5) + 5; i++) {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrderId(order.getId());
            orderProduct.setOrderSn(order.getOrderSn());
            orderProducts.add(orderProduct);
        }
        orderProducts.forEach(orderProductMapper::insert);
        for (int i = 0; i < orderProducts.size(); i++) {
            orderProducts.set(i, orderProductMapper.selectById(orderProducts.get(i).getId()));
        }
        OrderDetailDTO orderDetailDTO = orderService.getOrderDetail(order.getId());
        assertEquals(order, orderDetailDTO.getOrder());
        for (OrderProduct orderProduct : orderProducts) {
            assertTrue(orderDetailDTO.getOrderProducts().contains(orderProduct));
        }

        Order order1 = new Order();
        order1.setMemberId(1L);
        order1.setOrderSn("for test");
        orderMapper.insert(order1);
        order1 = orderMapper.selectById(order1.getId());
        OrderDetailDTO orderDetailDTO1 = orderService.getOrderDetail(order1.getId());
        assertEquals(order1, orderDetailDTO1.getOrder());
        assertTrue(CollectionUtils.isEmpty(orderDetailDTO1.getOrderProducts()));
    }
}