package online.ahayujie.mall.portal.oms.service;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.portal.TestBase;
import online.ahayujie.mall.portal.mms.bean.model.ReceiveAddress;
import online.ahayujie.mall.portal.mms.mapper.MemberMapper;
import online.ahayujie.mall.portal.mms.mapper.ReceiveAddressMapper;
import online.ahayujie.mall.portal.mms.service.MemberService;
import online.ahayujie.mall.portal.oms.bean.dto.*;
import online.ahayujie.mall.portal.oms.bean.model.Order;
import online.ahayujie.mall.portal.oms.mapper.OrderMapper;
import online.ahayujie.mall.portal.pms.bean.model.Product;
import online.ahayujie.mall.portal.pms.bean.model.Sku;
import online.ahayujie.mall.portal.pms.mapper.ProductMapper;
import online.ahayujie.mall.portal.pms.mapper.SkuMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class OrderServiceTest extends TestBase {
    @Autowired
    private OrderService orderService;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ReceiveAddressMapper receiveAddressMapper;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${jwt.header}")
    private String JWT_HEADER;
    @Value("${jwt.header-prefix}")
    private String JWT_HEADER_PREFIX;
    @Autowired
    private OrderMapper orderMapper;

    @BeforeEach
    void setUp() {
        initMember(passwordEncoder, memberMapper, memberService, JWT_HEADER, JWT_HEADER_PREFIX);
    }

    @Test
    void generateConfirmOrder() {
        Random random = new Random();
        Map<Sku, Product> skuProductMap = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            Product product = new Product();
            product.setName(getRandomString(10));
            product.setUnit(getRandomString(2));
            product.setGiftPoint(random.nextInt(1000));
            product.setUsePointLimit(random.nextInt(5000));
            if (i == 4) {
                product.setIsPublish(Product.PublishStatus.NOT_PUBLISH.getValue());
            } else {
                product.setIsPublish(Product.PublishStatus.PUBLISH.getValue());
            }
            productMapper.insert(product);
            Sku sku = new Sku();
            sku.setProductId(product.getId());
            sku.setPic("http://" + getRandomString(50) + ".jpg");
            sku.setSpecification(getRandomString(50));
            sku.setPrice(new BigDecimal(random.nextInt(1000)));
            skuMapper.insert(sku);
            skuProductMap.put(sku, product);
        }
        List<Sku> skus = new ArrayList<>(skuProductMap.keySet());

        // 购买的商品不能为空
        GenerateConfirmOrderParam param = new GenerateConfirmOrderParam();
        assertThrows(IllegalArgumentException.class, () -> orderService.generateConfirmOrder(param));
        // 购买商品的数量小于等于0
        GenerateConfirmOrderParam param2 = new GenerateConfirmOrderParam();
        GenerateConfirmOrderParam.Product productParam1 = new GenerateConfirmOrderParam.Product();
        productParam1.setSkuId(skus.get(0).getId());
        productParam1.setQuantity(0);
        param2.setProducts(Collections.singletonList(productParam1));
        assertThrows(IllegalArgumentException.class, () -> orderService.generateConfirmOrder(param2));
        // 商品不存在
        GenerateConfirmOrderParam param1 = new GenerateConfirmOrderParam();
        GenerateConfirmOrderParam.Product productParam = new GenerateConfirmOrderParam.Product();
        productParam.setSkuId(-1L);
        productParam.setQuantity(1);
        param1.setProducts(Collections.singletonList(productParam));
        assertThrows(IllegalArgumentException.class, () -> orderService.generateConfirmOrder(param1));
        // 商品未上架
        GenerateConfirmOrderParam param3 = new GenerateConfirmOrderParam();
        GenerateConfirmOrderParam.Product productParam2 = new GenerateConfirmOrderParam.Product();
        for (Sku sku : skus) {
            if (Product.PublishStatus.NOT_PUBLISH.getValue().equals(skuProductMap.get(sku).getIsPublish())) {
                productParam2.setSkuId(sku.getId());
                break;
            }
        }
        productParam2.setQuantity(1);
        param3.setProducts(Collections.singletonList(productParam2));
        assertThrows(IllegalArgumentException.class, () -> orderService.generateConfirmOrder(param3));
        // 收货地址不存在
        GenerateConfirmOrderParam param5 = new GenerateConfirmOrderParam();
        GenerateConfirmOrderParam.Product productParam3 = new GenerateConfirmOrderParam.Product();
        for (Sku sku : skus) {
            if (Product.PublishStatus.PUBLISH.getValue().equals(skuProductMap.get(sku).getIsPublish())) {
                productParam3.setSkuId(sku.getId());
                break;
            }
        }
        productParam3.setQuantity(1);
        param5.setProducts(Collections.singletonList(productParam3));
        param5.setReceiveAddressId(-1L);
        assertThrows(IllegalArgumentException.class, () -> orderService.generateConfirmOrder(param5));

        ReceiveAddress receiveAddress = new ReceiveAddress();
        receiveAddress.setMemberId(member.getId());
        receiveAddress.setName(getRandomString(3));
        receiveAddressMapper.insert(receiveAddress);

        // 使用的积分数量不合法
        GenerateConfirmOrderParam param6 = new GenerateConfirmOrderParam();
        GenerateConfirmOrderParam.Product productParam4 = new GenerateConfirmOrderParam.Product();
        Integer maxUseIntegration = 0;
        for (Sku sku : skus) {
            Product product = skuProductMap.get(sku);
            if (Product.PublishStatus.PUBLISH.getValue().equals(product.getIsPublish())) {
                maxUseIntegration += product.getUsePointLimit();
                productParam4.setSkuId(sku.getId());
                break;
            }
        }
        productParam4.setQuantity(1);
        param6.setProducts(Collections.singletonList(productParam4));
        param6.setReceiveAddressId(receiveAddress.getId());
        param6.setUsedIntegration(-1);
        assertThrows(IllegalArgumentException.class, () -> orderService.generateConfirmOrder(param6));
        member.setIntegration(10000);
        memberMapper.updateById(member);
        param6.setUsedIntegration(member.getIntegration() + 100);
        assertThrows(IllegalArgumentException.class, () -> orderService.generateConfirmOrder(param6));
        member.setIntegration(maxUseIntegration + 10000);
        memberMapper.updateById(member);
        param6.setUsedIntegration(maxUseIntegration + 1000);
        assertThrows(IllegalArgumentException.class, () -> orderService.generateConfirmOrder(param6));
        IntegrationRule integrationRule = orderService.getIntegrationRule();
        param6.setUsedIntegration(integrationRule.getIntegrationUseUnit() + 1);

        // legal
        GenerateConfirmOrderParam param4 = new GenerateConfirmOrderParam();
        param4.setProducts(new ArrayList<>());
        for (int i = 0; i < skus.size(); i++) {
            if (Product.PublishStatus.NOT_PUBLISH.getValue().equals(skuProductMap.get(skus.get(i)).getIsPublish())) {
                continue;
            }
            GenerateConfirmOrderParam.Product product = new GenerateConfirmOrderParam.Product();
            product.setSkuId(skus.get(i).getId());
            product.setQuantity(i + 1);
            param4.getProducts().add(product);
        }
        param4.setReceiveAddressId(receiveAddress.getId());
        member.setIntegration(10000);
        memberMapper.updateById(member);
        param4.setUsedIntegration(integrationRule.getIntegrationUseUnit());
        ConfirmOrderDTO confirmOrderDTO = orderService.generateConfirmOrder(param4);
        assertNotNull(confirmOrderDTO);
        assertEquals(param4.getProducts().size(), confirmOrderDTO.getProducts().size());
        for (ConfirmOrderDTO.Product productDTO : confirmOrderDTO.getProducts()) {
            GenerateConfirmOrderParam.Product test = null;
            for (GenerateConfirmOrderParam.Product compare : param4.getProducts()) {
                if (compare.getSkuId().equals(productDTO.getSkuId())) {
                    test = compare;
                    break;
                }
            }
            assertNotNull(test);
            assertEquals(test.getQuantity(), productDTO.getQuantity());
            Sku sku = null;
            for (Sku compare : skus) {
                if (compare.getId().equals(productDTO.getSkuId())) {
                    sku = compare;
                    break;
                }
            }
            assertNotNull(sku);
            assertEquals(sku.getProductId(), productDTO.getId());
        }
        log.debug(confirmOrderDTO.toString());
    }

    @Test
    void submit() {
        IntegrationRule integrationRule = orderService.getIntegrationRule();
        Random random = new Random();
        Map<Sku, Product> skuProductMap = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            Product product = new Product();
            product.setName(getRandomString(10));
            product.setUnit(getRandomString(2));
            product.setGiftPoint(random.nextInt(1000));
            product.setUsePointLimit(integrationRule.getIntegrationUseUnit() * 1000);
            if (i == 4) {
                product.setIsPublish(Product.PublishStatus.NOT_PUBLISH.getValue());
            } else {
                product.setIsPublish(Product.PublishStatus.PUBLISH.getValue());
            }
            productMapper.insert(product);
            Sku sku = new Sku();
            sku.setProductId(product.getId());
            sku.setPic("http://" + getRandomString(50) + ".jpg");
            sku.setSpecification(getRandomString(50));
            sku.setPrice(new BigDecimal(random.nextInt(1000)));
            sku.setStock(10);
            skuMapper.insert(sku);
            skuProductMap.put(sku, product);
        }
        List<Sku> skus = new ArrayList<>(skuProductMap.keySet());

        // 购买的商品不能为空
        SubmitOrderParam param = new SubmitOrderParam();
        assertThrows(IllegalArgumentException.class, () -> orderService.submit(param));
        // 购买商品的数量小于等于0
        SubmitOrderParam param2 = new SubmitOrderParam();
        SubmitOrderParam.Product productParam1 = new SubmitOrderParam.Product();
        productParam1.setSkuId(skus.get(0).getId());
        productParam1.setQuantity(0);
        param2.setProducts(Collections.singletonList(productParam1));
        assertThrows(IllegalArgumentException.class, () -> orderService.submit(param2));
        // 商品不存在
        SubmitOrderParam param1 = new SubmitOrderParam();
        SubmitOrderParam.Product productParam = new SubmitOrderParam.Product();
        productParam.setSkuId(-1L);
        productParam.setQuantity(1);
        param1.setProducts(Collections.singletonList(productParam));
        assertThrows(IllegalArgumentException.class, () -> orderService.submit(param1));
        // 商品未上架
        SubmitOrderParam param3 = new SubmitOrderParam();
        SubmitOrderParam.Product productParam2 = new SubmitOrderParam.Product();
        for (Sku sku : skus) {
            if (Product.PublishStatus.NOT_PUBLISH.getValue().equals(skuProductMap.get(sku).getIsPublish())) {
                productParam2.setSkuId(sku.getId());
                break;
            }
        }
        productParam2.setQuantity(1);
        param3.setProducts(Collections.singletonList(productParam2));
        assertThrows(IllegalArgumentException.class, () -> orderService.submit(param3));
        // 收货地址不存在
        SubmitOrderParam param5 = new SubmitOrderParam();
        SubmitOrderParam.Product productParam3 = new SubmitOrderParam.Product();
        for (Sku sku : skus) {
            if (Product.PublishStatus.PUBLISH.getValue().equals(skuProductMap.get(sku).getIsPublish())) {
                productParam3.setSkuId(sku.getId());
                break;
            }
        }
        productParam3.setQuantity(1);
        param5.setProducts(Collections.singletonList(productParam3));
        param5.setReceiveAddressId(-1L);
        assertThrows(IllegalArgumentException.class, () -> orderService.submit(param5));

        ReceiveAddress receiveAddress = new ReceiveAddress();
        receiveAddress.setMemberId(member.getId());
        receiveAddress.setName(getRandomString(3));
        receiveAddressMapper.insert(receiveAddress);

        // 使用的积分数量不合法
        SubmitOrderParam param6 = new SubmitOrderParam();
        SubmitOrderParam.Product productParam4 = new SubmitOrderParam.Product();
        productParam4.setQuantity(1);
        int maxUseIntegration = 0;
        for (Sku sku : skus) {
            Product product = skuProductMap.get(sku);
            if (Product.PublishStatus.PUBLISH.getValue().equals(product.getIsPublish())) {
                productParam4.setQuantity(sku.getStock());
                maxUseIntegration += (product.getUsePointLimit() * productParam4.getQuantity());
                productParam4.setSkuId(sku.getId());
                break;
            }
        }
        param6.setProducts(Collections.singletonList(productParam4));
        param6.setReceiveAddressId(receiveAddress.getId());
        param6.setUsedIntegration(-1);
        assertThrows(IllegalArgumentException.class, () -> orderService.submit(param6));
        member.setIntegration(10000);
        memberMapper.updateById(member);
        param6.setUsedIntegration(member.getIntegration() + 100);
        assertThrows(IllegalArgumentException.class, () -> orderService.submit(param6));
        member.setIntegration(maxUseIntegration + 10000);
        memberMapper.updateById(member);
        param6.setUsedIntegration(maxUseIntegration + 1000);
        assertThrows(IllegalArgumentException.class, () -> orderService.submit(param6));
        param6.setUsedIntegration(integrationRule.getIntegrationUseUnit() + 1);

        // 库存不足
        SubmitOrderParam param4 = new SubmitOrderParam();
        param4.setReceiveAddressId(receiveAddress.getId());
        param4.setUsedIntegration(0);
        SubmitOrderParam.Product product = new SubmitOrderParam.Product();
        product.setSkuId(skus.get(0).getId());
        product.setQuantity(skus.get(0).getStock() + 10);
        param4.setProducts(Collections.singletonList(product));
        assertThrows(IllegalArgumentException.class, () -> orderService.submit(param4));

        // legal
        SubmitOrderParam param7 = new SubmitOrderParam();
        param7.setReceiveAddressId(receiveAddress.getId());
        int usedIntegration = 0;
        List<SubmitOrderParam.Product> products = new ArrayList<>();
        for (Sku sku : skus) {
            if (skuProductMap.get(sku).getIsPublish().equals(Product.PublishStatus.PUBLISH.getValue())) {
                SubmitOrderParam.Product product1 = new SubmitOrderParam.Product();
                product1.setSkuId(sku.getId());
                product1.setQuantity(sku.getStock());
                usedIntegration += (skuProductMap.get(skus.get(0)).getUsePointLimit() * product1.getQuantity());
                products.add(product1);
            }
        }
        param7.setProducts(products);
        param7.setUsedIntegration(usedIntegration);
        member.setIntegration(usedIntegration);
        memberMapper.updateById(member);
        SubmitOrderDTO submitOrderDTO = orderService.submit(param7);
        Order order = orderMapper.selectById(submitOrderDTO.getId());
        assertNotNull(order);
        assertEquals(order.getOrderSn(), submitOrderDTO.getOrderSn());
        assertEquals(order.getPayAmount(), submitOrderDTO.getPayAmount());
        assertEquals(member.getId(), order.getMemberId());
    }
}