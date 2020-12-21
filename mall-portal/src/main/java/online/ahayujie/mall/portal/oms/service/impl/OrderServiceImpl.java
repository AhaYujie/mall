package online.ahayujie.mall.portal.oms.service.impl;

import online.ahayujie.mall.portal.config.RedisConfig;
import online.ahayujie.mall.portal.mapper.DictMapper;
import online.ahayujie.mall.portal.mms.bean.model.Member;
import online.ahayujie.mall.portal.mms.bean.model.ReceiveAddress;
import online.ahayujie.mall.portal.mms.service.MemberService;
import online.ahayujie.mall.portal.mms.service.ReceiveAddressService;
import online.ahayujie.mall.portal.oms.bean.dto.*;
import online.ahayujie.mall.portal.oms.bean.model.Order;
import online.ahayujie.mall.portal.oms.bean.model.OrderProduct;
import online.ahayujie.mall.portal.oms.mapper.OrderMapper;
import online.ahayujie.mall.portal.oms.mapper.OrderProductMapper;
import online.ahayujie.mall.portal.oms.publisher.OrderPublisher;
import online.ahayujie.mall.portal.oms.service.OrderService;
import online.ahayujie.mall.portal.pms.bean.model.Sku;
import online.ahayujie.mall.portal.pms.mapper.SkuMapper;
import online.ahayujie.mall.portal.pms.service.ProductService;
import online.ahayujie.mall.portal.pms.service.SkuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author aha
 * @since 2020-11-07
 */
@Service
public class OrderServiceImpl implements OrderService {
    private static final String INTEGRATION_DICT_CODE = "integration-setting";
    private static final String INTEGRATION_RATIO_DICT_KEY = "1";
    private static final String INTEGRATION_UNIT_DICT_KEY = "2";

    private SkuService skuService;
    private MemberService memberService;
    private ProductService productService;
    private OrderPublisher orderPublisher;
    private ReceiveAddressService receiveAddressService;

    private final SkuMapper skuMapper;
    private final DictMapper dictMapper;
    private final OrderMapper orderMapper;
    private final OrderProductMapper orderProductMapper;
    private final RedisTemplate<String, Serializable> redisTemplate;

    public OrderServiceImpl(SkuMapper skuMapper, DictMapper dictMapper, OrderMapper orderMapper,
                            OrderProductMapper orderProductMapper, RedisTemplate<String, Serializable> redisTemplate) {
        this.skuMapper = skuMapper;
        this.dictMapper = dictMapper;
        this.orderMapper = orderMapper;
        this.orderProductMapper = orderProductMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public ConfirmOrderDTO generateConfirmOrder(GenerateConfirmOrderParam param) throws IllegalArgumentException {
        // 商品信息检验和获取
        if (CollectionUtils.isEmpty(param.getProducts())) {
            throw new IllegalArgumentException("购买的商品不能为空");
        }
        // 合并相同商品
        List<GenerateConfirmOrderParam.Product> mergeProducts = new ArrayList<>();
        for (GenerateConfirmOrderParam.Product product : param.getProducts()) {
            GenerateConfirmOrderParam.Product compare = null;
            for (GenerateConfirmOrderParam.Product prev : mergeProducts) {
                if (product.getSkuId().equals(prev.getSkuId())) {
                    compare = prev;
                    break;
                }
            }
            if (compare == null) {
                compare = product;
                mergeProducts.add(compare);
            } else {
                compare.setQuantity(compare.getQuantity() + product.getQuantity());
            }
        }
        param.setProducts(mergeProducts);
        List<Long> skuIds = param.getProducts().stream().map(GenerateConfirmOrderParam.Product::getSkuId).collect(Collectors.toList());
        for (GenerateConfirmOrderParam.Product product : param.getProducts()) {
            if (product.getQuantity() <= 0) {
                throw new IllegalArgumentException("购买商品的数量小于等于0");
            }
        }
        List<Sku> skus = skuMapper.selectBatchIds(skuIds);
        if (skuIds.size() != skus.size()) {
            List<Long> exists = skus.stream().map(Sku::getId).collect(Collectors.toList());
            for (Long skuId : skuIds) {
                if (!exists.contains(skuId)) {
                    throw new IllegalArgumentException("商品不存在: " + skuId);
                }
            }
        }
        List<Long> productIds = skus.stream().map(Sku::getProductId).collect(Collectors.toList());
        List<ConfirmOrderDTO.Product> products = productService.getConfirmOrderProductBatch(productIds);
        Map<Long, ConfirmOrderDTO.Product> skuProductMap = new HashMap<>();
        for (Sku sku : skus) {
            ConfirmOrderDTO.Product product = null;
            for (ConfirmOrderDTO.Product compare : products) {
                if (sku.getProductId().equals(compare.getId())) {
                    product = compare;
                    break;
                }
            }
            if (product == null) {
                throw new IllegalArgumentException("商品不存在");
            }
            skuProductMap.put(sku.getId(), product);
        }
        for (ConfirmOrderDTO.Product product : products) {
            for (Sku sku : skus) {
                if (sku.getProductId().equals(product.getId())) {
                    product.setSkuId(sku.getId());
                    product.setPic(sku.getPic());
                    product.setSpecification(sku.getSpecification());
                    product.setPrice(sku.getPrice());
                    break;
                }
            }
            for (GenerateConfirmOrderParam.Product productParam : param.getProducts()) {
                if (productParam.getSkuId().equals(product.getSkuId())) {
                    product.setQuantity(productParam.getQuantity());
                    break;
                }
            }
        }

        // 会员收货地址检验和获取
        ConfirmOrderDTO.ReceiveAddress receiveAddressDTO = new ConfirmOrderDTO.ReceiveAddress();
        if (param.getReceiveAddressId() != null) {
            Member member = memberService.getMemberFromToken();
            ReceiveAddress receiveAddress = receiveAddressService.getById(param.getReceiveAddressId(), member.getId());
            if (receiveAddress == null) {
                throw new IllegalArgumentException("收货地址不存在");
            }
            BeanUtils.copyProperties(receiveAddress, receiveAddressDTO);
        }

        // 积分信息检验和获取
        ConfirmOrderDTO.IntegrationInfo integrationInfo = new ConfirmOrderDTO.IntegrationInfo();
        integrationInfo.setIntegration(memberService.getInfo().getIntegration());
        int maxUseIntegration = 0;
        for (GenerateConfirmOrderParam.Product product : param.getProducts()) {
            maxUseIntegration += (product.getQuantity() * skuProductMap.get(product.getSkuId()).getUsePointLimit());
        }
        integrationInfo.setMaxUseIntegration(maxUseIntegration);
        IntegrationRule integrationRule = getIntegrationRule();
        integrationInfo.setIntegrationRatio(integrationRule.getIntegrationRatio());
        integrationInfo.setIntegrationUseUnit(integrationRule.getIntegrationUseUnit());
        Integer usedIntegration = param.getUsedIntegration();
        if (usedIntegration != null) {
            integrationInfo.setUsedIntegration(usedIntegration);
            if (usedIntegration < 0 || usedIntegration > integrationInfo.getIntegration()) {
                throw new IllegalArgumentException("使用的积分数量不合法");
            }
            if (usedIntegration > maxUseIntegration) {
                throw new IllegalArgumentException("使用超过可以抵扣的积分数量");
            }
            if (usedIntegration % integrationInfo.getIntegrationUseUnit() != 0) {
                throw new IllegalArgumentException("使用的积分数量不合法");
            }
        }

        // 计算金额
        BigDecimal productAmount = new BigDecimal("0");
        for (ConfirmOrderDTO.Product product : products) {
            productAmount = productAmount.add(product.getPrice().multiply(new BigDecimal(product.getQuantity())));
        }
        BigDecimal integrationAmount = new BigDecimal(integrationInfo.getUsedIntegration())
                .divide(integrationInfo.getIntegrationRatio(), 2, BigDecimal.ROUND_HALF_UP);
        // TODO:计算订单运费
        BigDecimal freightAmount = new BigDecimal("0");
        BigDecimal payAmount = productAmount.add(freightAmount).subtract(integrationAmount);
        ConfirmOrderDTO.Amount amount = new ConfirmOrderDTO.Amount();
        amount.setIntegrationAmount(integrationAmount);
        amount.setProductAmount(productAmount);
        amount.setFreightAmount(freightAmount);
        amount.setPayAmount(payAmount);

        ConfirmOrderDTO confirmOrderDTO = new ConfirmOrderDTO();
        confirmOrderDTO.setProducts(products);
        confirmOrderDTO.setReceiveAddress(receiveAddressDTO);
        confirmOrderDTO.setIntegrationInfo(integrationInfo);
        confirmOrderDTO.setAmount(amount);
        return confirmOrderDTO;
    }

    @Override
    public IntegrationRule getIntegrationRule() {
        IntegrationRule integrationRule = new IntegrationRule();
        integrationRule.setIntegrationRatio(new BigDecimal(dictMapper.selectByCodeAndDictKey(INTEGRATION_DICT_CODE,
                INTEGRATION_RATIO_DICT_KEY).getDictValue()));
        integrationRule.setIntegrationUseUnit(Integer.valueOf(dictMapper.selectByCodeAndDictKey(INTEGRATION_DICT_CODE,
                INTEGRATION_UNIT_DICT_KEY).getDictValue()));
        return integrationRule;
    }

    @Override
    public SubmitOrderDTO submit(SubmitOrderParam param) throws IllegalArgumentException {
        Order order = new Order();
        order.setStatus(Order.Status.UN_PAY.getValue());
        order.setOrderType(Order.Type.NORMAL.getValue());
        order.setSourceType(Order.SourceType.APP.getValue());
        order.setPayType(Order.PayType.UN_PAY.getValue());

        // 商品信息检验
        if (CollectionUtils.isEmpty(param.getProducts())) {
            throw new IllegalArgumentException("购买的商品不能为空");
        }
        // 合并相同的商品
        List<SubmitOrderParam.Product> mergeProducts = new ArrayList<>();
        for (SubmitOrderParam.Product product : param.getProducts()) {
            SubmitOrderParam.Product merge = null;
            for (SubmitOrderParam.Product prev : mergeProducts) {
                if (prev.getSkuId().equals(product.getSkuId())) {
                    merge = prev;
                    break;
                }
            }
            if (merge == null) {
                merge = product;
                mergeProducts.add(merge);
            } else {
                merge.setQuantity(merge.getQuantity() + product.getQuantity());
            }
        }
        param.setProducts(mergeProducts);
        List<Long> skuIds = param.getProducts().stream().map(SubmitOrderParam.Product::getSkuId).collect(Collectors.toList());
        for (SubmitOrderParam.Product product : param.getProducts()) {
            if (product.getQuantity() <= 0) {
                throw new IllegalArgumentException("购买商品的数量小于等于0");
            }
        }
        List<Sku> skus = skuMapper.selectBatchIds(skuIds);
        Map<Long, Sku> idSkuMap = new HashMap<>();
        for (Long id : skuIds) {
            Sku sku = null;
            for (Sku compare : skus) {
                if (id.equals(compare.getId())) {
                    sku = compare;
                    break;
                }
            }
            if (sku == null) {
                throw new IllegalArgumentException("商品不存在");
            }
            idSkuMap.put(id, sku);
        }
        if (skuIds.size() != skus.size()) {
            throw new IllegalArgumentException("商品不存在");
        }
        List<Long> productIds = skus.stream().map(Sku::getProductId).collect(Collectors.toList());
        List<SubmitOrderProductDTO> productDTOS = productService.getSubmitOrderProductBatch(productIds);
        Map<Long, SubmitOrderProductDTO> skuProductMap = new HashMap<>();
        for (Sku sku : skus) {
            SubmitOrderProductDTO productDTO = null;
            for (SubmitOrderProductDTO compare : productDTOS) {
                if (sku.getProductId().equals(compare.getProductId())) {
                    productDTO = compare;
                    break;
                }
            }
            if (productDTO == null) {
                throw new IllegalArgumentException("商品不存在");
            }
            skuProductMap.put(sku.getId(), productDTO);
        }
        int integration = 0;
        List<OrderProduct> orderProducts = new ArrayList<>();
        for (SubmitOrderParam.Product product : param.getProducts()) {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setSkuId(product.getSkuId());
            SubmitOrderProductDTO productDTO = skuProductMap.get(product.getSkuId());
            BeanUtils.copyProperties(productDTO, orderProduct);
            Sku sku = idSkuMap.get(product.getSkuId());
            orderProduct.setProductSkuCode(sku.getSkuCode());
            orderProduct.setProductPic(sku.getPic());
            orderProduct.setProductPrice(sku.getPrice());
            orderProduct.setProductQuantity(product.getQuantity());
            orderProduct.setSpecification(sku.getSpecification());
            orderProduct.setStatus(OrderProduct.Status.UN_PAY.getValue());
            orderProduct.setIsComment(OrderProduct.UN_COMMENT);
            orderProducts.add(orderProduct);
            integration += (orderProduct.getIntegration() * orderProduct.getProductQuantity());
        }
        order.setIntegration(integration);

        // 会员收货地址检验
        Member member = memberService.getMemberFromToken();
        ReceiveAddress receiveAddress = receiveAddressService.getById(param.getReceiveAddressId(), member.getId());
        if (receiveAddress == null) {
            throw new IllegalArgumentException("收货地址不存在");
        }
        order.setMemberId(member.getId());
        order.setMemberUsername(member.getUsername());
        order.setReceiverName(receiveAddress.getName());
        order.setReceiverPhone(receiveAddress.getPhoneNumber());
        order.setReceiverProvince(receiveAddress.getProvince());
        order.setReceiverCity(receiveAddress.getCity());
        order.setReceiverRegion(receiveAddress.getRegion());
        order.setReceiverStreet(receiveAddress.getStreet());
        order.setReceiverDetailAddress(receiveAddress.getDetailAddress());

        // 积分信息检验
        Integer haveIntegration = memberService.getInfo().getIntegration();
        Integer usedIntegration = param.getUsedIntegration();
        if (usedIntegration < 0 || usedIntegration > haveIntegration) {
            throw new IllegalArgumentException("使用的积分数量不合法");
        }
        int maxUseIntegration = 0;
        for (SubmitOrderParam.Product product : param.getProducts()) {
            maxUseIntegration += (product.getQuantity() * skuProductMap.get(product.getSkuId()).getUsePointLimit());
        }
        if (usedIntegration > maxUseIntegration) {
            throw new IllegalArgumentException("使用超过可以抵扣的积分数量");
        }
        IntegrationRule integrationRule = getIntegrationRule();
        if (usedIntegration % integrationRule.getIntegrationUseUnit() != 0) {
            throw new IllegalArgumentException("使用的积分数量不合法");
        }
        order.setUseIntegration(usedIntegration);

        // 计算订单金额
        BigDecimal freightAmount = new BigDecimal("0");
        BigDecimal totalAmount = freightAmount;
        BigDecimal discountAmount = new BigDecimal("0");
        BigDecimal totalPromotionAmount = new BigDecimal("0");
        BigDecimal totalCouponAmount = new BigDecimal("0");
        Integer restIntegration = order.getUseIntegration();
        for (int i = 0; i < orderProducts.size(); i++) {
            OrderProduct orderProduct = orderProducts.get(i);
            SubmitOrderProductDTO productDTO = productDTOS.get(i);
            BigDecimal promotionAmount = new BigDecimal("0");
            BigDecimal couponAmount = new BigDecimal("0");
            Integer usePointLimit = productDTO.getUsePointLimit();
            int productUsedIntegration = (restIntegration >= usePointLimit ? usePointLimit : restIntegration);
            restIntegration -= productUsedIntegration;
            BigDecimal integrationAmount = new BigDecimal(productUsedIntegration)
                    .divide(integrationRule.getIntegrationRatio(), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal amount = orderProduct.getProductPrice().multiply(new BigDecimal(orderProduct.getProductQuantity()));
            totalAmount = totalAmount.add(amount);
            BigDecimal realAmount = amount.subtract(promotionAmount).subtract(couponAmount).subtract(integrationAmount);
            orderProduct.setPromotionAmount(promotionAmount);
            orderProduct.setCouponAmount(couponAmount);
            orderProduct.setIntegrationAmount(integrationAmount);
            orderProduct.setRealAmount(realAmount);
            totalPromotionAmount = totalPromotionAmount.add(promotionAmount);
            totalCouponAmount = totalCouponAmount.add(couponAmount);
        }
        BigDecimal integrationAmount = new BigDecimal(order.getUseIntegration())
                .divide(integrationRule.getIntegrationRatio(), 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal payAmount = totalAmount.subtract(discountAmount).subtract(totalPromotionAmount)
                .subtract(totalCouponAmount).subtract(integrationAmount);
        order.setFreightAmount(freightAmount);
        order.setTotalAmount(totalAmount);
        order.setDiscountAmount(discountAmount);
        order.setPromotionAmount(totalPromotionAmount);
        order.setCouponAmount(totalCouponAmount);
        order.setIntegrationAmount(integrationAmount);
        order.setPayAmount(payAmount);

        // 商品库存扣减
        try {
            skuService.updateStock(param.getProducts());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("商品库存不足");
        }

        order.setOrderSn(generateOrderSn(order));
        order.setCreateTime(new Date());
        orderMapper.insert(order);

        for (OrderProduct orderProduct: orderProducts) {
            orderProduct.setOrderId(order.getId());
            orderProduct.setOrderSn(order.getOrderSn());
            orderProduct.setCreateTime(new Date());
        }
        orderProductMapper.insertList(orderProducts);

        // 发送延迟消息到消息队列
        orderPublisher.publishOrderTimeoutCancelDelayedMsg(new OrderCancelMsgDTO(order.getId()));

        SubmitOrderDTO submitOrderDTO = new SubmitOrderDTO();
        BeanUtils.copyProperties(order, submitOrderDTO);
        return submitOrderDTO;
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

    @Autowired
    public void setSkuService(SkuService skuService) {
        this.skuService = skuService;
    }

    @Autowired
    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setOrderPublisher(OrderPublisher orderPublisher) {
        this.orderPublisher = orderPublisher;
    }

    @Autowired
    public void setReceiveAddressService(ReceiveAddressService receiveAddressService) {
        this.receiveAddressService = receiveAddressService;
    }
}
