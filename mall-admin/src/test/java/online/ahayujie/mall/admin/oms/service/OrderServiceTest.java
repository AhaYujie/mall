package online.ahayujie.mall.admin.oms.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.config.RedisConfig;
import online.ahayujie.mall.admin.mms.bean.model.Member;
import online.ahayujie.mall.admin.mms.bean.model.ReceiveAddress;
import online.ahayujie.mall.admin.mms.mapper.MemberMapper;
import online.ahayujie.mall.admin.mms.mapper.ReceiveAddressMapper;
import online.ahayujie.mall.admin.mms.service.MemberService;
import online.ahayujie.mall.admin.oms.bean.dto.*;
import online.ahayujie.mall.admin.oms.bean.model.Order;
import online.ahayujie.mall.admin.oms.bean.model.OrderProduct;
import online.ahayujie.mall.admin.oms.exception.IllegalOrderException;
import online.ahayujie.mall.admin.oms.mapper.OrderMapper;
import online.ahayujie.mall.admin.oms.mapper.OrderProductMapper;
import online.ahayujie.mall.admin.pms.bean.model.Brand;
import online.ahayujie.mall.admin.pms.bean.model.Product;
import online.ahayujie.mall.admin.pms.bean.model.ProductCategory;
import online.ahayujie.mall.admin.pms.bean.model.Sku;
import online.ahayujie.mall.admin.pms.mapper.BrandMapper;
import online.ahayujie.mall.admin.pms.mapper.ProductCategoryMapper;
import online.ahayujie.mall.admin.pms.mapper.ProductMapper;
import online.ahayujie.mall.admin.pms.mapper.SkuMapper;
import online.ahayujie.mall.common.api.CommonPage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderProductMapper orderProductMapper;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private ProductCategoryMapper productCategoryMapper;
    @Autowired
    private ReceiveAddressMapper receiveAddressMapper;

    @Test
    void list() {
        // not exist
        QueryOrderListParam param = new QueryOrderListParam();
        param.setOrderSn("不存在的订单号");
        CommonPage<OrderListDTO> result = orderService.list(1, 5, param);
        assertEquals(0, result.getData().size());
        QueryOrderListParam param1 = new QueryOrderListParam();
        param1.setStatus(-1);
        CommonPage<OrderListDTO> result1 = orderService.list(1, 5, param1);
        assertEquals(0, result1.getData().size());
        QueryOrderListParam param2 = new QueryOrderListParam();
        param2.setOrderType(-1);
        CommonPage<OrderListDTO> result2 = orderService.list(1, 5, param2);
        assertEquals(0, result2.getData().size());
        QueryOrderListParam param3 = new QueryOrderListParam();
        param3.setSourceType(-1);
        CommonPage<OrderListDTO> result3 = orderService.list(1, 5, param3);
        assertEquals(0, result3.getData().size());
        QueryOrderListParam param4 = new QueryOrderListParam();
        param4.setMemberUsername("不存在的用户名123456abcdefg");
        CommonPage<OrderListDTO> result4 = orderService.list(1, 5, param4);
        assertEquals(0, result4.getData().size());

        // exist
        List<Order> orders = orderMapper.selectList(Wrappers.emptyWrapper());
        orders.forEach(order -> orderMapper.deleteById(order.getId()));

        List<Order> orders1 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Order order = new Order();
            order.setMemberId(1L);
            order.setOrderSn("for test: " + i);
            orders1.add(order);
        }
        Order order = new Order();
        order.setMemberId(1L);
        order.setOrderSn("for test: query");
        order.setStatus(Order.Status.CLOSED.getValue());
        order.setOrderType(Order.Type.MIAO_SHA.getValue());
        order.setSourceType(Order.SourceType.APP.getValue());
        order.setMemberUsername("for test: member username");
        orders1.add(order);
        orders1.forEach(orderMapper::insert);
        CommonPage<OrderListDTO> result5 = orderService.list(1, 5, new QueryOrderListParam());
        assertEquals(5, result5.getData().size());
        CommonPage<OrderListDTO> result6 = orderService.list(3, 5, new QueryOrderListParam());
        assertEquals(1, result6.getData().size());

        QueryOrderListParam param5 = new QueryOrderListParam();
        param5.setOrderSn(order.getOrderSn());
        CommonPage<OrderListDTO> result11 = orderService.list(1, 5, param5);
        assertEquals(1, result11.getData().size());
        QueryOrderListParam param6 = new QueryOrderListParam();
        param6.setStatus(order.getStatus());
        CommonPage<OrderListDTO> result12 = orderService.list(1, 5, param6);
        assertEquals(1, result12.getData().size());
        QueryOrderListParam param7 = new QueryOrderListParam();
        param7.setOrderType(order.getOrderType());
        CommonPage<OrderListDTO> result13 = orderService.list(1, 5, param7);
        assertEquals(1, result13.getData().size());
        QueryOrderListParam param8 = new QueryOrderListParam();
        param8.setSourceType(order.getSourceType());
        CommonPage<OrderListDTO> result14 = orderService.list(1, 5, param8);
        assertEquals(1, result14.getData().size());
        QueryOrderListParam param9 = new QueryOrderListParam();
        param9.setMemberUsername(order.getMemberUsername());
        CommonPage<OrderListDTO> result15 = orderService.list(1, 5, param9);
        assertEquals(1, result15.getData().size());
    }

    @Test
    void getOrderDetail() {
        // not exist
        Throwable throwable = assertThrows(IllegalOrderException.class, () -> orderService.getOrderDetail(-1L));
        log.debug(throwable.getMessage());

        // exist
        Order order = new Order();
        order.setMemberId(1L);
        order.setOrderSn("for test");
        orderMapper.insert(order);
        order = orderMapper.selectById(order.getId());
        List<OrderProduct> orderProducts = new ArrayList<>();
        for (int i = 0; i < new Random().nextInt(5) + 5; i++) {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrderId(order.getId());
            orderProduct.setOrderSn(order.getOrderSn());
            orderProducts.add(orderProduct);
        }
        orderProducts.forEach(orderProductMapper::insert);
        for (int i = 0; i < orderProducts.size(); i++) {
            orderProducts.set(i, orderProductMapper.selectById(orderProducts.get(i).getId()));
        }
        OrderDetailDTO orderDetailDTO = orderService.getOrderDetail(order.getId());
        assertEquals(order, orderDetailDTO.getOrder());
        for (OrderProduct orderProduct : orderProducts) {
            assertTrue(orderDetailDTO.getOrderProducts().contains(orderProduct));
        }

        Order order1 = new Order();
        order1.setMemberId(1L);
        order1.setOrderSn("for test");
        orderMapper.insert(order1);
        order1 = orderMapper.selectById(order1.getId());
        OrderDetailDTO orderDetailDTO1 = orderService.getOrderDetail(order1.getId());
        assertEquals(order1, orderDetailDTO1.getOrder());
        assertTrue(CollectionUtils.isEmpty(orderDetailDTO1.getOrderProducts()));
    }

    @Test
    void createOrder() {
        // 测试检查创建订单提交的参数
        testCreateOrderParamValidate();
        // 测试检查库存
        testValidateStock();

        // 创建用户和默认收货地址
        Member member = createTestMember();
        ReceiveAddress receiveAddress = createTestReceiveAddress(member.getId());

        // 创建商品
        Product product = createTestProduct();
        Sku sku = createTestSku(product.getId());
        Sku sku1 = createTestSku(product.getId());

        CreateOrderParam param = new CreateOrderParam();
        param.setMemberId(member.getId());
        param.setDiscountAmount(new BigDecimal("100"));
        List<CreateOrderParam.Product> products = new ArrayList<>();
        CreateOrderParam.Product productParam = new CreateOrderParam.Product();
        productParam.setSkuId(sku.getId());
        productParam.setProductQuantity(10);
        products.add(productParam);
        CreateOrderParam.Product productParam1 = new CreateOrderParam.Product();
        productParam1.setSkuId(sku1.getId());
        productParam1.setProductQuantity(10);
        products.add(productParam1);
        param.setProducts(products);
        List<Order> orders = orderMapper.selectList(Wrappers.emptyWrapper());
        orderService.createOrder(param);
        List<Order> orders1 = orderMapper.selectList(Wrappers.emptyWrapper());
        Order order = null;
        for (Order order1 : orders1) {
            if (!orders.contains(order1)) {
                order = order1;
                break;
            }
        }
        assertNotNull(order);

        assertEquals(member.getId(), order.getMemberId());
        assertEquals(member.getUsername(), order.getMemberUsername());
        assertEquals(Order.Status.UN_PAY.getValue(), order.getStatus());
        assertEquals(Order.Type.NORMAL.getValue(), order.getOrderType());
        assertEquals(Order.SourceType.PC.getValue(), order.getSourceType());
        BigDecimal totalAmount = order.getFreightAmount()
                .add(sku.getPrice().multiply(new BigDecimal(productParam.getProductQuantity())))
                .add(sku1.getPrice().multiply(new BigDecimal(productParam1.getProductQuantity())));
        BigDecimal payAmount = totalAmount.subtract(param.getDiscountAmount());
        assertEquals(0, order.getTotalAmount().compareTo(totalAmount));
        assertEquals(0, order.getPayAmount().compareTo(payAmount));
        assertEquals(0, order.getPromotionAmount().compareTo(new BigDecimal("0")));
        assertEquals(0, order.getIntegrationAmount().compareTo(new BigDecimal("0")));
        assertEquals(0, order.getCouponAmount().compareTo(new BigDecimal("0")));
        assertEquals(0, order.getDiscountAmount().compareTo(param.getDiscountAmount()));
        assertEquals(Order.PayType.UN_PAY.getValue(), order.getPayType());
        assertEquals(receiveAddress.getName(), order.getReceiverName());
        assertEquals(receiveAddress.getPhoneNumber(), order.getReceiverPhone());
        assertEquals(receiveAddress.getProvince(), order.getReceiverProvince());
        assertEquals(receiveAddress.getCity(), order.getReceiverCity());
        assertEquals(receiveAddress.getRegion(), order.getReceiverRegion());
        assertEquals(receiveAddress.getStreet(), order.getReceiverStreet());
        assertEquals(receiveAddress.getDetailAddress(), order.getReceiverDetailAddress());

        List<OrderProduct> orderProducts = orderProductMapper.selectByOrderId(order.getId());
        assertEquals(2, orderProducts.size());
        for (OrderProduct orderProduct : orderProducts) {
            Sku compare = null;
            CreateOrderParam.Product compare1 = null;
            if (orderProduct.getSkuId().equals(sku.getId())) {
                compare = sku;
                compare1 = productParam;
            } else if (orderProduct.getSkuId().equals(sku1.getId())) {
                compare = sku1;
                compare1 = productParam1;
            }
            assertNotNull(compare);
            assertNotNull(compare1);
            assertEquals(order.getOrderSn(), orderProduct.getOrderSn());
            assertEquals(product.getId(), orderProduct.getProductId());
            assertEquals(compare.getSkuCode(), orderProduct.getProductSkuCode());
            assertEquals(product.getProductSn(), orderProduct.getProductSn());
            assertEquals(product.getName(), orderProduct.getProductName());
            assertEquals(product.getBrandName(), orderProduct.getBrandName());
            assertEquals(product.getProductCategoryName(), orderProduct.getProductCategoryName());
            assertEquals(compare.getPic(), orderProduct.getProductPic());
            assertEquals(0, orderProduct.getProductPrice().compareTo(compare.getPrice()));
            assertEquals(compare1.getProductQuantity(), orderProduct.getProductQuantity());
            assertEquals(compare.getSpecification(), orderProduct.getSpecification());
            assertEquals(0, orderProduct.getRealAmount().compareTo(compare.getPrice()));
            assertEquals(product.getGiftPoint(), orderProduct.getIntegration());
        }
    }

    private void testCreateOrderParamValidate() {
        Member member = createTestMember();
        // 会员不存在
        CreateOrderParam param = new CreateOrderParam();
        param.setMemberId(-1L);
        param.setDiscountAmount(new BigDecimal("0"));
        CreateOrderParam.Product product = new CreateOrderParam.Product();
        product.setSkuId(1L);
        product.setProductQuantity(1);
        param.setProducts(Collections.singletonList(product));
        Throwable throwable = assertThrows(IllegalOrderException.class, () -> orderService.createOrder(param));
        log.debug(throwable.getMessage());
        // 折扣金额小于0
        CreateOrderParam param1 = new CreateOrderParam();
        param1.setMemberId(member.getId());
        param1.setDiscountAmount(new BigDecimal("-1"));
        CreateOrderParam.Product product1 = new CreateOrderParam.Product();
        product1.setSkuId(1L);
        product1.setProductQuantity(1);
        param1.setProducts(Collections.singletonList(product1));
        Throwable throwable1 = assertThrows(IllegalOrderException.class, () -> orderService.createOrder(param1));
        log.debug(throwable1.getMessage());
        // 购买的商品为空
        CreateOrderParam param2 = new CreateOrderParam();
        param2.setMemberId(member.getId());
        param2.setDiscountAmount(new BigDecimal("0"));
        param2.setProducts(new ArrayList<>());
        Throwable throwable2 = assertThrows(IllegalOrderException.class, () -> orderService.createOrder(param2));
        log.debug(throwable2.getMessage());
    }

    private void testValidateStock() {
        Member member = createTestMember();
        Sku sku = new Sku();
        sku.setProductId(1L);
        sku.setStock(10);
        skuMapper.insert(sku);

        // sku不存在
        CreateOrderParam param = new CreateOrderParam();
        param.setMemberId(member.getId());
        param.setDiscountAmount(new BigDecimal("0"));
        CreateOrderParam.Product product = new CreateOrderParam.Product();
        product.setSkuId(-1L);
        product.setProductQuantity(1);
        param.setProducts(Collections.singletonList(product));
        Throwable throwable = assertThrows(IllegalOrderException.class, () -> orderService.createOrder(param));
        log.debug(throwable.getMessage());

        // sku库存不足
        CreateOrderParam param1 = new CreateOrderParam();
        param1.setMemberId(member.getId());
        param1.setDiscountAmount(new BigDecimal("0"));
        CreateOrderParam.Product product1 = new CreateOrderParam.Product();
        product1.setSkuId(sku.getId());
        product1.setProductQuantity(11);
        param1.setProducts(Collections.singletonList(product1));
        Throwable throwable1 = assertThrows(IllegalOrderException.class, () -> orderService.createOrder(param1));
        log.debug(throwable1.getMessage());
    }

    private static String getRandomString(int length) {
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    private static String getRandomNum(int length) {
        String str = "0123456789";
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(random.nextInt(str.length()));
        }
        return stringBuilder.toString();
    }

    private Member createTestMember() {
        Member member = new Member();
        member.setUsername(getRandomString(40));
        member.setPhone(getRandomNum(11));
        memberMapper.insert(member);
        return member;
    }

    private ReceiveAddress createTestReceiveAddress(Long memberId) {
        ReceiveAddress receiveAddress = new ReceiveAddress();
        receiveAddress.setMemberId(memberId);
        receiveAddress.setName(getRandomString(10));
        receiveAddress.setPhoneNumber(getRandomString(11));
        receiveAddress.setProvince(getRandomString(5));
        receiveAddress.setCity(getRandomString(5));
        receiveAddress.setRegion(getRandomString(5));
        receiveAddress.setStreet(getRandomString(5));
        receiveAddress.setDetailAddress(getRandomString(20));
        receiveAddress.setIsDefault(ReceiveAddress.DEFAULT);
        receiveAddressMapper.insert(receiveAddress);
        return receiveAddress;
    }

    private Product createTestProduct() {
        Brand brand = new Brand();
        brand.setName(getRandomString(10));
        brandMapper.insert(brand);
        ProductCategory productCategory = new ProductCategory();
        productCategory.setName(getRandomString(10));
        productCategoryMapper.insert(productCategory);
        Product product = new Product();
        product.setBrandId(brand.getId());
        product.setBrandName(brand.getName());
        product.setProductCategoryId(productCategory.getId());
        product.setProductCategoryName(productCategory.getName());
        product.setName(getRandomString(10));
        product.setPic(getRandomString(100));
        product.setGiftPoint(100);
        productMapper.insert(product);
        return product;
    }

    private Sku createTestSku(Long productId) {
        Sku sku = new Sku();
        sku.setProductId(productId);
        sku.setSkuCode(getRandomString(20));
        sku.setPrice(new BigDecimal("99.99"));
        sku.setStock(100);
        sku.setPic(getRandomString(100));
        sku.setSpecification(getRandomString(50));
        skuMapper.insert(sku);
        return sku;
    }

    @Test
    void generateOrderSn() {
        Order order = new Order();
        order.setSourceType(Order.SourceType.PC.getValue());
        order.setOrderType(Order.Type.NORMAL.getValue());
        String orderSn = orderService.generateOrderSn(order);
        log.debug("订单号：" + orderSn);
        assertEquals(order.getSourceType(), Integer.valueOf(orderSn.substring(12, 14)));
        assertEquals(order.getOrderType(), Integer.valueOf(orderSn.substring(14, 16)));
    }

    @Test
    void deliverOrder() {
        // illegal
        // 订单不存在
        DeliverOrderParam param1 = new DeliverOrderParam();
        param1.setId(-1L);
        param1.setDeliverySn("1234567");
        param1.setDeliveryCompany("东风快递");
        param1.setDeliveryTime(new Date());
        Throwable throwable = assertThrows(IllegalOrderException.class, () -> orderService.deliverOrder(param1));
        log.debug(throwable.getMessage());
        // 订单状态不支持此操作
        Order order1 = new Order();
        order1.setMemberId(1L);
        order1.setStatus(Order.Status.UN_PAY.getValue());
        orderMapper.insert(order1);
        DeliverOrderParam param2 = new DeliverOrderParam();
        param2.setId(order1.getId());
        param2.setDeliverySn("1234567");
        param2.setDeliveryCompany("东风快递");
        param2.setDeliveryTime(new Date());
        Throwable throwable1 = assertThrows(UnsupportedOperationException.class, () -> orderService.deliverOrder(param2));
        log.debug(throwable1.getMessage());

        // legal
        Order order = new Order();
        order.setMemberId(1L);
        order.setStatus(Order.Status.UN_DELIVER.getValue());
        orderMapper.insert(order);
        DeliverOrderParam param = new DeliverOrderParam();
        param.setId(order.getId());
        param.setDeliverySn("1234567");
        param.setDeliveryCompany("东风快递");
        param.setDeliveryTime(new Date());
        orderService.deliverOrder(param);
        order = orderMapper.selectById(order.getId());
        assertEquals(param.getDeliverySn(), order.getDeliverySn());
        assertEquals(param.getDeliveryCompany(), order.getDeliveryCompany());
        assertEquals(Order.Status.DELIVERED.getValue(), order.getStatus());
    }

    @Test
    void refuseAfterSaleApply() {
        // 当前订单不支持此操作
        Order order = new Order();
        order.setMemberId(1L);
        order.setStatus(Order.Status.COMPLETE.getValue());
        orderMapper.insert(order);
        assertThrows(UnsupportedOperationException.class, () -> orderService.refuseAfterSaleApply(order.getId(), null));

        // legal
        Order order1 = new Order();
        order1.setMemberId(1L);
        order1.setStatus(Order.Status.APPLY_REFUND.getValue());
        orderMapper.insert(order1);
        orderService.refuseAfterSaleApply(order1.getId(), new ArrayList<>());
        Order order2 = new Order();
        order2.setMemberId(1L);
        order2.setStatus(Order.Status.APPLY_RETURN.getValue());
        orderMapper.insert(order2);
        orderService.refuseAfterSaleApply(order2.getId(), new ArrayList<>());
    }

    @Test
    void agreeAfterSaleApply() {
        // 当前订单不支持此操作
        Order order = new Order();
        order.setMemberId(1L);
        order.setStatus(Order.Status.COMPLETE.getValue());
        orderMapper.insert(order);
        assertThrows(UnsupportedOperationException.class, () -> orderService.agreeAfterSaleApply(order.getId()));

        // legal
        Order order1 = new Order();
        order1.setMemberId(1L);
        order1.setStatus(Order.Status.APPLY_REFUND.getValue());
        orderMapper.insert(order1);
        orderService.agreeAfterSaleApply(order1.getId());
        Order order2 = new Order();
        order2.setMemberId(1L);
        order2.setStatus(Order.Status.APPLY_RETURN.getValue());
        orderMapper.insert(order2);
        orderService.agreeAfterSaleApply(order2.getId());
    }
}