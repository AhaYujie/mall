package online.ahayujie.mall.portal.oms.service;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.portal.TestBase;
import online.ahayujie.mall.portal.mms.bean.model.ReceiveAddress;
import online.ahayujie.mall.portal.mms.mapper.MemberMapper;
import online.ahayujie.mall.portal.mms.mapper.ReceiveAddressMapper;
import online.ahayujie.mall.portal.mms.service.MemberService;
import online.ahayujie.mall.portal.oms.bean.dto.ConfirmOrderDTO;
import online.ahayujie.mall.portal.oms.bean.dto.GenerateConfirmOrderParam;
import online.ahayujie.mall.portal.oms.bean.dto.IntegrationRule;
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
}