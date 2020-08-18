package online.ahayujie.mall.admin.oms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.config.RabbitmqConfig;
import online.ahayujie.mall.admin.oms.bean.dto.*;
import online.ahayujie.mall.admin.oms.bean.model.Order;
import online.ahayujie.mall.admin.oms.bean.model.OrderProduct;
import online.ahayujie.mall.admin.oms.exception.IllegalOrderException;
import online.ahayujie.mall.admin.oms.mapper.OrderMapper;
import online.ahayujie.mall.admin.oms.mapper.OrderProductMapper;
import online.ahayujie.mall.admin.oms.publisher.OrderPublisher;
import online.ahayujie.mall.admin.oms.service.OrderContext;
import online.ahayujie.mall.admin.oms.service.OrderContextFactory;
import online.ahayujie.mall.admin.oms.service.OrderService;
import online.ahayujie.mall.common.api.CommonPage;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author aha
 * @since 2020-08-08
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    private OrderPublisher orderPublisher;
    private OrderContextFactory orderContextFactory;

    private final OrderMapper orderMapper;
    private final ObjectMapper objectMapper;
    private final OrderProductMapper orderProductMapper;

    public OrderServiceImpl(OrderMapper orderMapper, ObjectMapper objectMapper, OrderProductMapper orderProductMapper) {
        this.orderMapper = orderMapper;
        this.objectMapper = objectMapper;
        this.orderProductMapper = orderProductMapper;
    }

    @Override
    public CommonPage<OrderListDTO> list(Integer pageNum, Integer pageSize, QueryOrderListParam param) {
        Page<OrderListDTO> page = new Page<>(pageNum, pageSize);
        IPage<OrderListDTO> orderListDTOPage = orderMapper.queryList(page, param);
        return new CommonPage<>(orderListDTOPage);
    }

    @Override
    public OrderDetailDTO getOrderDetail(Long id) throws IllegalOrderException {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new IllegalOrderException("订单不存在");
        }
        List<OrderProduct> orderProducts = orderProductMapper.selectByOrderId(id);
        return new OrderDetailDTO(order, orderProducts);
    }

    @Override
    public void createOrder(CreateOrderParam param) {
        OrderCancelMsgDTO orderCancelMsgDTO = new OrderCancelMsgDTO(12L);
        orderPublisher.publishOrderTimeoutCancelDelayedMsg(orderCancelMsgDTO);
    }

    @Override
    @RabbitListener(queues = RabbitmqConfig.ORDER_TIMEOUT_CANCEL_QUEUE)
    public void listenTimeoutCancel(Channel channel, Message message) throws IOException {
        try {
            OrderCancelMsgDTO orderCancelMsgDTO = objectMapper.readValue(message.getBody(), OrderCancelMsgDTO.class);
            Integer status = orderMapper.selectOrderStatus(orderCancelMsgDTO.getId());
            OrderContext orderContext = orderContextFactory.getOrderContext(status);
            orderContext.cancelTimeoutOrder(orderCancelMsgDTO.getId());
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (NullPointerException | UnsupportedOperationException e) {
            log.warn(e.toString());
            log.warn(Arrays.toString(e.getStackTrace()));
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

    @Override
    @RabbitListener(queues = RabbitmqConfig.ORDER_MEMBER_CANCEL_QUEUE)
    public void listenMemberCancel(Channel channel, Message message) throws IOException {
        try {
            OrderCancelMsgDTO orderCancelMsgDTO = objectMapper.readValue(message.getBody(), OrderCancelMsgDTO.class);
            Integer status = orderMapper.selectOrderStatus(orderCancelMsgDTO.getId());
            OrderContext orderContext = orderContextFactory.getOrderContext(status);
            orderContext.memberCancelOrder(orderCancelMsgDTO.getId());
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (NullPointerException | UnsupportedOperationException e) {
            log.warn(e.toString());
            log.warn(Arrays.toString(e.getStackTrace()));
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

    @Autowired
    public void setOrderPublisher(OrderPublisher orderPublisher) {
        this.orderPublisher = orderPublisher;
    }

    @Autowired
    public void setOrderContextFactory(OrderContextFactory orderContextFactory) {
        this.orderContextFactory = orderContextFactory;
    }
}
