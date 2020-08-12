package online.ahayujie.mall.admin.oms.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.oms.bean.dto.CreateOrderRefundReasonParam;
import online.ahayujie.mall.admin.oms.bean.dto.UpdateOrderRefundReasonParam;
import online.ahayujie.mall.admin.oms.bean.model.CompanyAddress;
import online.ahayujie.mall.admin.oms.bean.model.OrderRefundReason;
import online.ahayujie.mall.admin.oms.exception.IllegalOrderRefundReasonException;
import online.ahayujie.mall.admin.oms.mapper.OrderRefundReasonMapper;
import online.ahayujie.mall.common.api.CommonPage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class OrderRefundReasonServiceTest {
    @Autowired
    private OrderRefundReasonService orderRefundReasonService;
    @Autowired
    private OrderRefundReasonMapper orderRefundReasonMapper;

    @Test
    void create() {
        // illegal
        CreateOrderRefundReasonParam param = new CreateOrderRefundReasonParam();
        param.setStatus(-1);
        Throwable throwable = assertThrows(IllegalOrderRefundReasonException.class, () -> orderRefundReasonService.create(param));
        log.debug(throwable.getMessage());

        // legal
        CreateOrderRefundReasonParam param1 = new CreateOrderRefundReasonParam();
        param1.setName("for test");
        param1.setStatus(OrderRefundReason.ACTIVE_STATUS);
        param1.setSort(100);
        List<OrderRefundReason> oldOrderRefundReasons = orderRefundReasonMapper.selectList(Wrappers.emptyWrapper());
        orderRefundReasonService.create(param1);
        List<OrderRefundReason> newOrderRefundReasons = orderRefundReasonMapper.selectList(Wrappers.emptyWrapper());
        assertEquals(oldOrderRefundReasons.size() + 1, newOrderRefundReasons.size());
    }

    @Test
    void update() {
        OrderRefundReason orderRefundReason = new OrderRefundReason();
        orderRefundReason.setName("for test");
        orderRefundReasonMapper.insert(orderRefundReason);
        Long id = orderRefundReason.getId();

        // illegal
        UpdateOrderRefundReasonParam param = new UpdateOrderRefundReasonParam();
        param.setStatus(-1);
        Throwable throwable = assertThrows(IllegalOrderRefundReasonException.class, () -> orderRefundReasonService.update(param, id));
        log.debug(throwable.getMessage());

        // legal
        UpdateOrderRefundReasonParam param1 = new UpdateOrderRefundReasonParam();
        param1.setName("update");
        param1.setSort(100);
        param1.setStatus(OrderRefundReason.ACTIVE_STATUS);
        orderRefundReasonService.update(param1, id);
        OrderRefundReason newOrderRefundReason = orderRefundReasonMapper.selectById(id);
        UpdateOrderRefundReasonParam compare = new UpdateOrderRefundReasonParam();
        BeanUtils.copyProperties(newOrderRefundReason, compare);
        assertEquals(param1, compare);
    }

    @Test
    void delete() {
        Random random = new Random();
        List<OrderRefundReason> orderRefundReasons = new ArrayList<>();
        for (int i = 0; i < random.nextInt(20) + 5; i++) {
            OrderRefundReason orderRefundReason = new OrderRefundReason();
            orderRefundReason.setName("for test: " + i);
            orderRefundReasons.add(orderRefundReason);
        }
        orderRefundReasons.forEach(orderRefundReasonMapper::insert);

        List<OrderRefundReason> orderRefundReasons1 = orderRefundReasonMapper.selectList(Wrappers.emptyWrapper());
        orderRefundReasons.forEach(orderRefundReason -> orderRefundReasonService.delete(orderRefundReason.getId()));
        List<OrderRefundReason> orderRefundReasons2 = orderRefundReasonMapper.selectList(Wrappers.emptyWrapper());
        assertEquals(orderRefundReasons1.size() - orderRefundReasons.size(), orderRefundReasons2.size());
    }

    @Test
    void list() {
        List<OrderRefundReason> orderRefundReasons = orderRefundReasonMapper.selectList(Wrappers.emptyWrapper());
        orderRefundReasons.forEach(orderRefundReason -> orderRefundReasonMapper.deleteById(orderRefundReason.getId()));

        int size = 10;
        List<OrderRefundReason> orderRefundReasons1 = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            OrderRefundReason orderRefundReason = new OrderRefundReason();
            orderRefundReason.setName("for test: " + i);
            orderRefundReasons1.add(orderRefundReason);
        }
        orderRefundReasons1.forEach(orderRefundReasonMapper::insert);

        CommonPage<OrderRefundReason> result = orderRefundReasonService.list(1, 6);
        log.debug("result: " + result);
        assertEquals(6, result.getData().size());
        CommonPage<OrderRefundReason> result1 = orderRefundReasonService.list(2, 6);
        log.debug("result1: " + result1);
        assertEquals(4, result1.getData().size());
    }
}