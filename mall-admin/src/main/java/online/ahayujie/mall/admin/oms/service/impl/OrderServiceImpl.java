package online.ahayujie.mall.admin.oms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.config.RabbitmqConfig;
import online.ahayujie.mall.admin.config.RedisConfig;
import online.ahayujie.mall.admin.mms.bean.model.Member;
import online.ahayujie.mall.admin.mms.bean.model.ReceiveAddress;
import online.ahayujie.mall.admin.mms.service.MemberService;
import online.ahayujie.mall.admin.mms.service.ReceiveAddressService;
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
import online.ahayujie.mall.admin.pms.bean.model.Product;
import online.ahayujie.mall.admin.pms.bean.model.Sku;
import online.ahayujie.mall.admin.pms.mapper.ProductMapper;
import online.ahayujie.mall.admin.pms.service.ProductService;
import online.ahayujie.mall.admin.pms.service.SkuService;
import online.ahayujie.mall.common.api.CommonPage;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

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
    private SkuService skuService;
    private ProductService productService;
    private MemberService memberService;
    private OrderPublisher orderPublisher;
    private OrderContextFactory orderContextFactory;
    private ReceiveAddressService receiveAddressService;

    private final OrderMapper orderMapper;
    private final ObjectMapper objectMapper;
    private final OrderProductMapper orderProductMapper;
    private final RedisTemplate<String, Serializable> redisTemplate;

    public OrderServiceImpl(OrderMapper orderMapper, ObjectMapper objectMapper, OrderProductMapper orderProductMapper,
                            RedisTemplate<String, Serializable> redisTemplate) {
        this.orderMapper = orderMapper;
        this.objectMapper = objectMapper;
        this.orderProductMapper = orderProductMapper;
        this.redisTemplate = redisTemplate;
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
    public void createOrder(CreateOrderParam param) throws IllegalOrderException {
        // 检查创建订单提交的参数
        validateCreateOrderParam(param);
        Order order = new Order();
        // 检查会员是否存在
        Member member = memberService.getById(param.getMemberId());
        if (member == null) {
            throw new IllegalOrderException("会员不存在");
        }
        order.setMemberId(member.getId());
        order.setMemberUsername(member.getUsername());

        // 生成订单商品信息
        Integer integration = 0;
        List<OrderProduct> orderProducts = new ArrayList<>();
        for (CreateOrderParam.Product productParam : param.getProducts()) {
            OrderProduct orderProduct = new OrderProduct();
            Sku sku = skuService.getById(productParam.getSkuId());
            // 检查库存
            if (sku == null || sku.getStock() < productParam.getProductQuantity()) {
                throw new IllegalOrderException("商品库存不足");
            }
            Product product =  productService.getById(sku.getProductId());
            orderProduct.setProductId(product.getId());
            orderProduct.setSkuId(sku.getId());
            orderProduct.setProductSkuCode(sku.getSkuCode());
            orderProduct.setProductSn(product.getProductSn());
            orderProduct.setProductName(product.getName());
            orderProduct.setBrandName(product.getBrandName());
            orderProduct.setProductCategoryName(product.getProductCategoryName());
            orderProduct.setProductPic(sku.getPic());
            orderProduct.setProductPrice(sku.getPrice());
            orderProduct.setProductQuantity(productParam.getProductQuantity());
            orderProduct.setSpecification(sku.getSpecification());
            orderProduct.setIntegration(product.getGiftPoint());
            orderProducts.add(orderProduct);
            integration += orderProduct.getIntegration();
        }
        order.setIntegration(integration);

        // 计算订单价格
        // TODO:计算订单运费
        order.setFreightAmount(new BigDecimal("0"));
        BigDecimal totalAmount = order.getFreightAmount();
        for (OrderProduct orderProduct : orderProducts) {
            BigDecimal realAmount = orderProduct.getProductPrice();
            orderProduct.setRealAmount(realAmount);
            totalAmount = totalAmount.add(realAmount.multiply(new BigDecimal(orderProduct.getProductQuantity())));
        }
        order.setTotalAmount(totalAmount);
        order.setDiscountAmount(param.getDiscountAmount());
        BigDecimal payAmount = order.getTotalAmount()
                .subtract(order.getDiscountAmount());
        order.setPayAmount(payAmount);

        // 订单状态，类型，来源
        order.setStatus(Order.Status.UN_PAY.getValue());
        order.setOrderType(Order.Type.NORMAL.getValue());
        order.setSourceType(Order.SourceType.PC.getValue());

        // 订单收货地址
        ReceiveAddress receiveAddress = receiveAddressService.getDefaultReceiveAddress(member.getId());
        if (receiveAddress != null) {
            order.setReceiverName(receiveAddress.getName());
            order.setReceiverPhone(receiveAddress.getPhoneNumber());
            order.setReceiverProvince(receiveAddress.getProvince());
            order.setReceiverCity(receiveAddress.getCity());
            order.setReceiverRegion(receiveAddress.getRegion());
            order.setReceiverStreet(receiveAddress.getStreet());
            order.setReceiverDetailAddress(receiveAddress.getDetailAddress());
        }

        // 生成订单号
        order.setOrderSn(generateOrderSn(order));

        // 保存订单信息
        order.setCreateTime(new Date());
        orderMapper.insert(order);
        // 保存订单商品信息
        for (OrderProduct orderProduct : orderProducts) {
            orderProduct.setOrderId(order.getId());
            orderProduct.setOrderSn(order.getOrderSn());
            orderProduct.setCreateTime(new Date());
            orderProductMapper.insert(orderProduct);
        }

        // 发送延迟消息到消息队列
        OrderCancelMsgDTO orderCancelMsgDTO = new OrderCancelMsgDTO(order.getId());
        orderPublisher.publishOrderTimeoutCancelDelayedMsg(orderCancelMsgDTO);
    }

    /**
     * 检查创建订单提交的参数
     * @param param 创建订单提交的参数
     */
    private void validateCreateOrderParam(CreateOrderParam param) throws IllegalOrderException {
        // 检查管理员后台调整订单使用的折扣金额是否合法
        if (param.getDiscountAmount().compareTo(new BigDecimal("0")) < 0) {
            throw new IllegalOrderException("折扣金额小于0");
        }
        // 检查购买的商品是否为空
        if (CollectionUtils.isEmpty(param.getProducts())) {
            throw new IllegalOrderException("购买的商品为空");
        }
        // 检查购买的商品数量是否合法
        for (CreateOrderParam.Product product : param.getProducts()) {
            if (product.getProductQuantity() == null || product.getProductQuantity() <= 0) {
                throw new IllegalOrderException("购买的商品数量不合法");
            }
        }
    }

    @Override
    public String generateOrderSn(Order order) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        // 日期(yyyyMMddHH格式)
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHH");
        String date = simpleDateFormat.format(new Date());
        stringBuilder.append(date);
        // 2位随机数
        stringBuilder.append(String.format("%02d", random.nextInt(100)));
        // 2位订单来源(不足2位补0)
        stringBuilder.append(String.format("%02d", order.getSourceType()));
        // 2位订单类型(不足2位补0)
        stringBuilder.append(String.format("%02d", order.getOrderType()));
        // 2位随机数
        stringBuilder.append(String.format("%02d", random.nextInt(100)));
        // 6位以上自增id(不足6位补0)
        String key = RedisConfig.ORDER_KEY_PREFIX + "sn.id." + date.substring(0, date.length() - 2)
                + RedisConfig.STRING_KEY_SUFFIX;
        Long id = redisTemplate.opsForValue().increment(key);
        String idStr = Objects.requireNonNull(id).toString();
        if (idStr.length() >= 6) {
            stringBuilder.append(idStr);
        } else {
            stringBuilder.append(String.format("%06d", id));
        }
        return stringBuilder.toString();
    }

    @Override
    public void deliverOrder(DeliverOrderParam param) throws IllegalOrderException, UnsupportedOperationException {
        Integer status = orderMapper.selectOrderStatus(param.getId());
        if (status == null) {
            throw new IllegalOrderException("订单不存在");
        }
        OrderContext orderContext = orderContextFactory.getOrderContext(status);
        orderContext.deliverOrder(param);
    }

    @Override
    public void refuseAfterSaleApply(Long orderId, List<Long> orderProductIds) throws UnsupportedOperationException {
        Integer status = orderMapper.selectOrderStatus(orderId);
        OrderContext orderContext = orderContextFactory.getOrderContext(status);
        orderContext.refuseAfterSaleApply(orderId, orderProductIds);
    }

    @Override
    public void agreeAfterSaleApply(Long orderId) throws UnsupportedOperationException {
        Integer status = orderMapper.selectOrderStatus(orderId);
        OrderContext orderContext = orderContextFactory.getOrderContext(status);
        orderContext.agreeAfterSaleApply(orderId);
    }

    @Override
    public void completeAfterSale(Long orderId, List<Long> orderProductIds) throws UnsupportedOperationException {
        Integer status = orderMapper.selectOrderStatus(orderId);
        OrderContext orderContext = orderContextFactory.getOrderContext(status);
        orderContext.completeAfterSale(orderId, orderProductIds);
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
        } catch (NullPointerException e) {
            log.warn(e.toString());
            log.warn(Arrays.toString(e.getStackTrace()));
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
        } catch (UnsupportedOperationException e) {
            // 订单状态已经不是未支付状态，取消操作
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
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
    public void setSkuService(SkuService skuService) {
        this.skuService = skuService;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
    }

    @Autowired
    public void setOrderPublisher(OrderPublisher orderPublisher) {
        this.orderPublisher = orderPublisher;
    }

    @Autowired
    public void setOrderContextFactory(OrderContextFactory orderContextFactory) {
        this.orderContextFactory = orderContextFactory;
    }

    @Autowired
    public void setReceiveAddressService(ReceiveAddressService receiveAddressService) {
        this.receiveAddressService = receiveAddressService;
    }
}
