package online.ahayujie.mall.admin.oms.service;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.oms.bean.dto.OrderReturnApplyDetailDTO;
import online.ahayujie.mall.admin.oms.bean.dto.RefuseOrderReturnApplyParam;
import online.ahayujie.mall.admin.oms.bean.model.*;
import online.ahayujie.mall.admin.oms.exception.IllegalCompanyAddressException;
import online.ahayujie.mall.admin.oms.exception.IllegalOrderReturnApplyException;
import online.ahayujie.mall.admin.oms.mapper.*;
import online.ahayujie.mall.common.api.CommonPage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
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
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderProductMapper orderProductMapper;
    @Autowired
    private CompanyAddressMapper companyAddressMapper;

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

    @Test
    void refuseApply() {
        // legal
        Order order = new Order();
        order.setMemberId(1L);
        order.setStatus(Order.Status.APPLY_RETURN.getValue());
        orderMapper.insert(order);
        List<OrderProduct> orderProducts = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrderId(order.getId());
            orderProduct.setStatus(OrderProduct.Status.AFTER_SALE.getValue());
            orderProducts.add(orderProduct);
        }
        orderProducts.forEach(orderProductMapper::insert);
        OrderReturnApply apply = new OrderReturnApply();
        apply.setMemberId(order.getMemberId());
        apply.setOrderId(order.getId());
        apply.setStatus(OrderReturnApply.Status.APPLYING.getValue());
        orderReturnApplyMapper.insert(apply);
        apply = orderReturnApplyMapper.selectById(apply.getId());
        List<OrderReturnApplyProduct> products = new ArrayList<>();
        for (OrderProduct orderProduct : orderProducts) {
            OrderReturnApplyProduct product = new OrderReturnApplyProduct();
            product.setOrderReturnApplyId(apply.getId());
            product.setOrderProductId(orderProduct.getId());
            products.add(product);
        }
        products.forEach(orderReturnApplyProductMapper::insert);
        RefuseOrderReturnApplyParam param = new RefuseOrderReturnApplyParam();
        param.setId(apply.getId());
        param.setHandleNote("test");
        orderReturnApplyService.refuseApply(param);
        apply = orderReturnApplyMapper.selectById(apply.getId());
        assertEquals(OrderReturnApply.Status.REFUSED.getValue(), apply.getStatus());
        assertEquals(param.getHandleNote(), apply.getHandleNote());

        // 申请不存在
        RefuseOrderReturnApplyParam param1 = new RefuseOrderReturnApplyParam();
        param1.setId(-1L);
        param1.setHandleNote("test");
        assertThrows(IllegalOrderReturnApplyException.class, () -> orderReturnApplyService.refuseApply(param1));

        // 当前申请不支持此操作
        assertThrows(IllegalOrderReturnApplyException.class, () -> orderReturnApplyService.refuseApply(param));
    }

    @Test
    void agreeApply() {
        // legal
        Order order = new Order();
        order.setMemberId(1L);
        order.setStatus(Order.Status.APPLY_REFUND.getValue());
        orderMapper.insert(order);
        OrderReturnApply apply = new OrderReturnApply();
        apply.setMemberId(order.getMemberId());
        apply.setOrderId(order.getId());
        apply.setStatus(OrderReturnApply.Status.APPLYING.getValue());
        orderReturnApplyMapper.insert(apply);
        apply = orderReturnApplyMapper.selectById(apply.getId());
        orderReturnApplyService.agreeApply(apply.getId());
        apply = orderReturnApplyMapper.selectById(apply.getId());
        assertEquals(OrderReturnApply.Status.PROCESSING.getValue(), apply.getStatus());

        // 申请不存在
        assertThrows(IllegalOrderReturnApplyException.class, () -> orderReturnApplyService.agreeApply(-1L));

        // 当前申请不支持此操作
        Long applyId = apply.getId();
        assertThrows(IllegalOrderReturnApplyException.class, () -> orderReturnApplyService.agreeApply(applyId));

    }

    @Test
    void setCompanyAddress() {
        // legal
        OrderReturnApply apply = new OrderReturnApply();
        apply.setOrderId(1L);
        apply.setMemberId(1L);
        orderReturnApplyMapper.insert(apply);
        CompanyAddress address = new CompanyAddress();
        address.setName("name");
        address.setReceiverName("receiverName");
        address.setReceiverPhone("phone");
        address.setProvince("province");
        address.setCity("city");
        address.setRegion("region");
        address.setStreet("street");
        address.setDetailAddress("detailAddress");
        companyAddressMapper.insert(address);
        orderReturnApplyService.setCompanyAddress(apply.getId(), address.getId());
        apply = orderReturnApplyMapper.selectById(apply.getId());
        assertEquals(address.getId(), apply.getCompanyAddressId());
        assertEquals(address.getName(), apply.getCompanyAddressName());
        assertEquals(address.getReceiverName(), apply.getReceiverName());
        assertEquals(address.getReceiverPhone(), apply.getReceiverPhone());
        assertEquals(address.getProvince(), apply.getProvince());
        assertEquals(address.getCity(), apply.getCity());
        assertEquals(address.getRegion(), apply.getRegion());
        assertEquals(address.getStreet(), apply.getStreet());
        assertEquals(address.getDetailAddress(), apply.getDetailAddress());

        // illegal
        Long applyId = apply.getId();
        assertThrows(IllegalOrderReturnApplyException.class, () -> orderReturnApplyService.setCompanyAddress(-1L, address.getId()));
        assertThrows(IllegalCompanyAddressException.class, () -> orderReturnApplyService.setCompanyAddress(applyId, -1L));
    }

    @Test
    void receive() {
        // illegal
        assertThrows(IllegalOrderReturnApplyException.class, () -> orderReturnApplyService.receive(-1L, null, null));

        // legal
        OrderReturnApply apply = new OrderReturnApply();
        apply.setOrderId(1L);
        apply.setMemberId(1L);
        orderReturnApplyMapper.insert(apply);
        String receiveNote = "note";
        Date receiveDate = new Date();
        orderReturnApplyService.receive(apply.getId(), receiveNote, receiveDate);
        apply = orderReturnApplyMapper.selectById(apply.getId());
        assertEquals(receiveNote, apply.getReceiveNote());
    }

    @Test
    void complete() {
        // legal
        Order order = new Order();
        order.setMemberId(1L);
        order.setStatus(Order.Status.RETURN.getValue());
        orderMapper.insert(order);
        List<OrderProduct> orderProducts = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrderId(order.getId());
            orderProduct.setStatus(OrderProduct.Status.AFTER_SALE.getValue());
            orderProducts.add(orderProduct);
        }
        orderProducts.forEach(orderProductMapper::insert);
        OrderReturnApply apply = new OrderReturnApply();
        apply.setMemberId(order.getMemberId());
        apply.setOrderId(order.getId());
        apply.setStatus(OrderReturnApply.Status.PROCESSING.getValue());
        orderReturnApplyMapper.insert(apply);
        apply = orderReturnApplyMapper.selectById(apply.getId());
        List<OrderReturnApplyProduct> products = new ArrayList<>();
        for (OrderProduct orderProduct : orderProducts) {
            OrderReturnApplyProduct product = new OrderReturnApplyProduct();
            product.setOrderReturnApplyId(apply.getId());
            product.setOrderProductId(orderProduct.getId());
            products.add(product);
        }
        products.forEach(orderReturnApplyProductMapper::insert);
        String handleNote = "note";
        orderReturnApplyService.complete(apply.getId(), handleNote);
        apply = orderReturnApplyMapper.selectById(apply.getId());
        assertEquals(OrderReturnApply.Status.COMPLETED.getValue(), apply.getStatus());
        assertEquals(handleNote, apply.getHandleNote());

        // 申请不存在
        assertThrows(IllegalOrderReturnApplyException.class, () -> orderReturnApplyService.complete(-1L, handleNote));

        // 当前申请不支持此操作
        Long applyId = apply.getId();
        assertThrows(IllegalOrderReturnApplyException.class, () -> orderReturnApplyService.complete(applyId, handleNote));

    }
}