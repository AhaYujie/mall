package online.ahayujie.mall.admin.oms.service;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.oms.bean.dto.OrderReturnApplyDetailDTO;
import online.ahayujie.mall.admin.oms.bean.model.Order;
import online.ahayujie.mall.admin.oms.bean.model.OrderReturnApply;
import online.ahayujie.mall.admin.oms.bean.model.OrderReturnApplyProduct;
import online.ahayujie.mall.admin.oms.mapper.OrderReturnApplyMapper;
import online.ahayujie.mall.admin.oms.mapper.OrderReturnApplyProductMapper;
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
class OrderReturnApplyServiceTest {
    @Autowired
    private OrderReturnApplyMapper orderReturnApplyMapper;
    @Autowired
    private OrderReturnApplyService orderReturnApplyService;
    @Autowired
    private OrderReturnApplyProductMapper orderReturnApplyProductMapper;

    @Test
    void list() {
        List<OrderReturnApply> applies = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            OrderReturnApply apply = new OrderReturnApply();
            apply.setOrderId(1L);
            apply.setMemberId(1L);
            applies.add(apply);
        }
        applies.forEach(orderReturnApplyMapper::insert);
        CommonPage<OrderReturnApply> result = orderReturnApplyService.list(1L, 5L);
        assertEquals(5, result.getData().size());
        CommonPage<OrderReturnApply> result1 = orderReturnApplyService.list(2L, 5L);
        assertEquals(5, result1.getData().size());
    }

    @Test
    void query() {
        String orderSn = "orderSn";
        Integer status = Order.Status.CLOSED.getValue();
        List<OrderReturnApply> applies = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            OrderReturnApply apply = new OrderReturnApply();
            apply.setOrderId(1L);
            apply.setMemberId(1L);
            apply.setOrderSn(orderSn);
            apply.setStatus(status);
            applies.add(apply);
        }
        applies.forEach(orderReturnApplyMapper::insert);
        CommonPage<OrderReturnApply> result = orderReturnApplyService.query(1L, 5L, orderSn, status);
        assertEquals(5, result.getData().size());
        CommonPage<OrderReturnApply> result1 = orderReturnApplyService.query(2L, 5L, orderSn, status);
        assertEquals(5, result1.getData().size());
    }

    @Test
    void queryByOrderId() {
        // not exist
        List<OrderReturnApply> orderReturnApplies = orderReturnApplyService.queryByOrderId(-1L);
        assertEquals(0, orderReturnApplies.size());

        // exist
        Long orderId = 1L;
        List<OrderReturnApply> applies = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            OrderReturnApply apply = new OrderReturnApply();
            apply.setOrderId(orderId);
            apply.setMemberId(1L);
            applies.add(apply);
        }
        applies.forEach(orderReturnApplyMapper::insert);
        List<OrderReturnApply> orderReturnApplies1 = orderReturnApplyService.queryByOrderId(orderId);
        assertEquals(applies.size(), orderReturnApplies1.size());
    }

    @Test
    void getDetailById() {
        // not exist
        OrderReturnApplyDetailDTO detailDTO = orderReturnApplyService.getDetailById(-1L);
        assertNull(detailDTO);

        // exist
        OrderReturnApply apply = new OrderReturnApply();
        apply.setMemberId(1L);
        apply.setOrderId(1L);
        orderReturnApplyMapper.insert(apply);
        apply = orderReturnApplyMapper.selectById(apply.getId());
        List<OrderReturnApplyProduct> products = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            OrderReturnApplyProduct product = new OrderReturnApplyProduct();
            product.setOrderReturnApplyId(apply.getId());
            products.add(product);
        }
        products.forEach(orderReturnApplyProductMapper::insert);
        OrderReturnApplyDetailDTO detailDTO1 = orderReturnApplyService.getDetailById(apply.getId());
        OrderReturnApply compare = new OrderReturnApply();
        BeanUtils.copyProperties(detailDTO1, compare);
        assertEquals(apply, compare);
        assertEquals(products.size(), detailDTO1.getProducts().size());
    }
}