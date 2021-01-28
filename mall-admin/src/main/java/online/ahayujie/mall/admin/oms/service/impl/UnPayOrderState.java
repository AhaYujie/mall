package online.ahayujie.mall.admin.oms.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.mms.service.MemberService;
import online.ahayujie.mall.admin.oms.bean.dto.CreateOrderParam;
import online.ahayujie.mall.admin.oms.bean.dto.OrderCancelledMsgDTO;
import online.ahayujie.mall.admin.oms.bean.model.Order;
import online.ahayujie.mall.admin.oms.bean.model.OrderProduct;
import online.ahayujie.mall.admin.oms.mapper.OrderMapper;
import online.ahayujie.mall.admin.oms.mapper.OrderProductMapper;
import online.ahayujie.mall.admin.oms.publisher.OrderPublisher;
import online.ahayujie.mall.admin.oms.service.AbstractOrderState;
import online.ahayujie.mall.admin.oms.service.OrderContext;
import online.ahayujie.mall.admin.oms.service.OrderContextFactory;
import online.ahayujie.mall.admin.oms.service.OrderState;
import online.ahayujie.mall.admin.pms.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 未付款订单状态 Service
 * @author aha
 * @since 2020/8/15
 */
@Slf4j
@Service(value = Order.UN_PAY_STATUS_NAME)
public class UnPayOrderState extends AbstractOrderState {
    private SkuService skuService;
    private MemberService memberService;
    private OrderPublisher orderPublisher;

    private final OrderMapper orderMapper;
    private final OrderProductMapper orderProductMapper;

    public UnPayOrderState(ApplicationContext applicationContext, OrderMapper orderMapper, OrderProductMapper orderProductMapper) {
        super(applicationContext);
        this.orderMapper = orderMapper;
        this.orderProductMapper = orderProductMapper;
    }

    @Override
    @PostConstruct
    protected void initOrderStateMap() {
        super.initOrderStateMap();
    }

    /**
     * 因为可能在取消订单的同时，用户正在支付订单，所以使用
     * redis分布式锁给订单加锁。
     * 若加锁失败或释放锁失败，则取消关闭订单。
     *
     * @see #memberCancelOrder(OrderContext, Long)
     * @param orderContext orderContext
     * @param id 订单id
     */
    @Override
    public void cancelTimeoutOrder(OrderContext orderContext, Long id) {
        // TODO:给订单加锁
        memberCancelOrder(orderContext, id);
    }

    /**
     * 取消超时未支付的订单，返还用户使用积分，恢复商品库存。
     * 订单取消后状态变为 {@link Order.Status#CLOSED}。
     * 取消订单成功后，发送消息到消息队列。
     *
     * @param orderContext orderContext
     * @param id 订单id
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void memberCancelOrder(OrderContext orderContext, Long id) {
        log.info("测试");
        // 返还用户积分
        Order order = orderMapper.selectById(id);
        memberService.updateIntegration(order.getMemberId(), order.getUseIntegration());
        // 恢复商品库存
        List<OrderProduct> orderProducts = orderProductMapper.selectByOrderId(id);
        List<CreateOrderParam.Product> products = new ArrayList<>();
        for (OrderProduct orderProduct : orderProducts) {
            CreateOrderParam.Product product = new CreateOrderParam.Product();
            product.setSkuId(orderProduct.getSkuId());
            product.setProductQuantity(-orderProduct.getProductQuantity());
            products.add(product);
        }
        skuService.updateStock(products);
        // 更新订单状态
        order = new Order();
        order.setId(id);
        order.setStatus(Order.Status.CLOSED.getValue());
        order.setUpdateTime(new Date());
        orderMapper.updateById(order);
        OrderCancelledMsgDTO orderCancelledMsgDTO = new OrderCancelledMsgDTO(id);
        orderPublisher.publishOrderCancelledMsg(orderCancelledMsgDTO);
        orderContext.setOrderState(getOrderState(Order.Status.CLOSED));
    }

    @Autowired
    public void setSkuService(SkuService skuService) {
        this.skuService = skuService;
    }

    @Autowired
    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
    }

    @Autowired
    public void setOrderPublisher(OrderPublisher orderPublisher) {
        this.orderPublisher = orderPublisher;
    }
}
