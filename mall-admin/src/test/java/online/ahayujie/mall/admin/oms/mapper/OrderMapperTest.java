package online.ahayujie.mall.admin.oms.mapper;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.oms.bean.model.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class OrderMapperTest {
    @Autowired
    private OrderMapper orderMapper;

    @Test
    void selectOrderStatus() {
        Order order = new Order();
        order.setMemberId(1L);
        order.setStatus(Order.Status.UN_PAY.getValue());
        orderMapper.insert(order);
        assertEquals(order.getStatus(), orderMapper.selectOrderStatus(order.getId()));
    }
}