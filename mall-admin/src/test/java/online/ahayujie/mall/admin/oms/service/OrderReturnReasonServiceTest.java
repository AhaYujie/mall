package online.ahayujie.mall.admin.oms.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.oms.bean.dto.CreateOrderRefundReasonParam;
import online.ahayujie.mall.admin.oms.bean.dto.CreateOrderReturnReasonParam;
import online.ahayujie.mall.admin.oms.bean.dto.UpdateOrderRefundReasonParam;
import online.ahayujie.mall.admin.oms.bean.dto.UpdateOrderReturnReasonParam;
import online.ahayujie.mall.admin.oms.bean.model.OrderRefundReason;
import online.ahayujie.mall.admin.oms.bean.model.OrderReturnReason;
import online.ahayujie.mall.admin.oms.exception.IllegalOrderRefundReasonException;
import online.ahayujie.mall.admin.oms.exception.IllegalOrderReturnReasonException;
import online.ahayujie.mall.admin.oms.mapper.OrderReturnReasonMapper;
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
class OrderReturnReasonServiceTest {
    @Autowired
    private OrderReturnReasonService orderReturnReasonService;
    @Autowired
    private OrderReturnReasonMapper orderReturnReasonMapper;

    @Test
    void create() {
        // illegal
        CreateOrderReturnReasonParam param = new CreateOrderReturnReasonParam();
        param.setStatus(-1);
        Throwable throwable = assertThrows(IllegalOrderReturnReasonException.class, () -> orderReturnReasonService.create(param));
        log.debug(throwable.getMessage());

        // legal
        CreateOrderReturnReasonParam param1 = new CreateOrderReturnReasonParam();
        param1.setName("for test");
        param1.setStatus(OrderRefundReason.ACTIVE_STATUS);
        param1.setSort(100);
        List<OrderReturnReason> oldOrderReturnReasons = orderReturnReasonMapper.selectList(Wrappers.emptyWrapper());
        orderReturnReasonService.create(param1);
        List<OrderReturnReason> newOrderReturnReasons = orderReturnReasonMapper.selectList(Wrappers.emptyWrapper());
        assertEquals(oldOrderReturnReasons.size() + 1, newOrderReturnReasons.size());
    }

    @Test
    void update() {
        OrderReturnReason orderReturnReason = new OrderReturnReason();
        orderReturnReason.setName("for test");
        orderReturnReasonMapper.insert(orderReturnReason);
        Long id = orderReturnReason.getId();

        // illegal
        UpdateOrderReturnReasonParam param = new UpdateOrderReturnReasonParam();
        param.setStatus(-1);
        Throwable throwable = assertThrows(IllegalOrderReturnReasonException.class, () -> orderReturnReasonService.update(id, param));
        log.debug(throwable.getMessage());

        // legal
        UpdateOrderReturnReasonParam param1 = new UpdateOrderReturnReasonParam();
        param1.setName("update");
        param1.setSort(100);
        param1.setStatus(OrderRefundReason.ACTIVE_STATUS);
        orderReturnReasonService.update(id, param1);
        OrderReturnReason orderReturnReason1 = orderReturnReasonMapper.selectById(id);
        UpdateOrderReturnReasonParam compare = new UpdateOrderReturnReasonParam();
        BeanUtils.copyProperties(orderReturnReason1, compare);
        assertEquals(param1, compare);
    }

    @Test
    void delete() {
        Random random = new Random();
        List<OrderReturnReason> orderReturnReasons = new ArrayList<>();
        for (int i = 0; i < random.nextInt(20) + 5; i++) {
            OrderReturnReason orderReturnReason = new OrderReturnReason();
            orderReturnReason.setName("for test: " + i);
            orderReturnReasons.add(orderReturnReason);
        }
        orderReturnReasons.forEach(orderReturnReasonMapper::insert);

        List<OrderReturnReason> orderReturnReasons1 = orderReturnReasonMapper.selectList(Wrappers.emptyWrapper());
        orderReturnReasons.forEach(orderReturnReason -> orderReturnReasonService.delete(orderReturnReason.getId()));
        List<OrderReturnReason> orderReturnReasons2 = orderReturnReasonMapper.selectList(Wrappers.emptyWrapper());
        assertEquals(orderReturnReasons1.size() - orderReturnReasons.size(), orderReturnReasons2.size());
    }

    @Test
    void list() {
        List<OrderReturnReason> orderReturnReasons = orderReturnReasonMapper.selectList(Wrappers.emptyWrapper());
        orderReturnReasons.forEach(orderReturnReason -> orderReturnReasonMapper.deleteById(orderReturnReason.getId()));

        int size = 10;
        List<OrderReturnReason> orderReturnReasons1 = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            OrderReturnReason orderReturnReason = new OrderReturnReason();
            orderReturnReason.setName("for test: " + i);
            orderReturnReasons1.add(orderReturnReason);
        }
        orderReturnReasons1.forEach(orderReturnReasonMapper::insert);

        CommonPage<OrderReturnReason> result = orderReturnReasonService.list(1, 6);
        log.debug("result: " + result);
        assertEquals(6, result.getData().size());
        CommonPage<OrderReturnReason> result1 = orderReturnReasonService.list(2, 6);
        log.debug("result1: " + result1);
        assertEquals(4, result1.getData().size());
    }
}