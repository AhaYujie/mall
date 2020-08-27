package online.ahayujie.mall.admin.oms.service;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.oms.bean.dto.OrderRefundApplyDetailDTO;
import online.ahayujie.mall.admin.oms.bean.model.Order;
import online.ahayujie.mall.admin.oms.bean.model.OrderRefundApply;
import online.ahayujie.mall.admin.oms.bean.model.OrderRefundApplyProduct;
import online.ahayujie.mall.admin.oms.mapper.OrderRefundApplyMapper;
import online.ahayujie.mall.admin.oms.mapper.OrderRefundApplyProductMapper;
import online.ahayujie.mall.common.api.CommonPage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class OrderRefundApplyServiceTest {
    @Autowired
    private OrderRefundApplyMapper orderRefundApplyMapper;
    @Autowired
    private OrderRefundApplyService orderRefundApplyService;
    @Autowired
    private OrderRefundApplyProductMapper orderRefundApplyProductMapper;

    @Test
    void list() {
        List<OrderRefundApply> orderRefundApplies = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            OrderRefundApply orderRefundApply = new OrderRefundApply();
            orderRefundApply.setOrderId(1L);
            orderRefundApply.setMemberId(1L);
            orderRefundApplies.add(orderRefundApply);
        }
        orderRefundApplies.forEach(orderRefundApplyMapper::insert);
        CommonPage<OrderRefundApply> result = orderRefundApplyService.list(1L, 5L);
        assertEquals(5, result.getData().size());
        CommonPage<OrderRefundApply> result1 = orderRefundApplyService.list(2L, 5L);
        assertEquals(5, result1.getData().size());
    }

    @Test
    void query() {
        String orderSn = "orderSn";
        Integer status = Order.Status.CLOSED.getValue();
        List<OrderRefundApply> orderRefundApplies = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            OrderRefundApply orderRefundApply = new OrderRefundApply();
            orderRefundApply.setOrderId(1L);
            orderRefundApply.setMemberId(1L);
            orderRefundApply.setOrderSn(orderSn);
            orderRefundApply.setStatus(status);
            orderRefundApplies.add(orderRefundApply);
        }
        orderRefundApplies.forEach(orderRefundApplyMapper::insert);
        CommonPage<OrderRefundApply> result = orderRefundApplyService.query(1L, 5L, orderSn, null);
        assertEquals(5, result.getData().size());
        CommonPage<OrderRefundApply> result1 = orderRefundApplyService.query(2L, 5L, orderSn, null);
        assertEquals(5, result1.getData().size());
        CommonPage<OrderRefundApply> result2 = orderRefundApplyService.query(1L, 5L, null, status);
        assertEquals(5, result2.getData().size());
        CommonPage<OrderRefundApply> result3 = orderRefundApplyService.query(2L, 5L, null, status);
        assertEquals(5, result3.getData().size());
    }

    @Test
    void queryByOrderId() {
        // exist
        Long orderId = 123456L;
        List<OrderRefundApply> orderRefundApplies = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            OrderRefundApply orderRefundApply = new OrderRefundApply();
            orderRefundApply.setOrderId(orderId);
            orderRefundApply.setMemberId(1L);
            orderRefundApplies.add(orderRefundApply);
        }
        orderRefundApplies.forEach(orderRefundApplyMapper::insert);
        List<OrderRefundApply> orderRefundApplies1 = orderRefundApplyService.queryByOrderId(orderId);
        assertTrue(orderRefundApplies1.size() >= orderRefundApplies.size());

        // not exist
        List<OrderRefundApply> orderRefundApplies2 = orderRefundApplyService.queryByOrderId(-1L);
        assertEquals(0, orderRefundApplies2.size());
    }

    @Test
    void getById() {
        // exist
        OrderRefundApply orderRefundApply = new OrderRefundApply();
        orderRefundApply.setOrderId(1L);
        orderRefundApply.setMemberId(1L);
        orderRefundApplyMapper.insert(orderRefundApply);
        orderRefundApply = orderRefundApplyMapper.selectById(orderRefundApply.getId());
        List<OrderRefundApplyProduct> products = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            OrderRefundApplyProduct product = new OrderRefundApplyProduct();
            product.setOrderRefundApplyId(orderRefundApply.getId());
            products.add(product);
        }
        products.forEach(orderRefundApplyProductMapper::insert);
        OrderRefundApplyDetailDTO detailDTO = orderRefundApplyService.getDetailById(orderRefundApply.getId());
        OrderRefundApply compare = new OrderRefundApply();
        BeanUtils.copyProperties(detailDTO, compare);
        assertEquals(orderRefundApply, compare);
        assertEquals(products.size(), detailDTO.getProducts().size());

        // not exist
        OrderRefundApplyDetailDTO detailDTO1 = orderRefundApplyService.getDetailById(-1L);
        assertNull(detailDTO1);
    }
}