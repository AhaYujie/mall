package online.ahayujie.mall.portal.oms.service;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.portal.TestBase;
import online.ahayujie.mall.portal.mms.mapper.MemberMapper;
import online.ahayujie.mall.portal.mms.service.MemberService;
import online.ahayujie.mall.portal.oms.bean.dto.AddCartProductParam;
import online.ahayujie.mall.portal.oms.bean.dto.CartProductDTO;
import online.ahayujie.mall.portal.oms.bean.model.CartProduct;
import online.ahayujie.mall.portal.oms.mapper.CartProductMapper;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class CartProductServiceTest extends TestBase {
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private CartProductMapper cartProductMapper;
    @Autowired
    private CartProductService cartProductService;
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
    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        initMember(passwordEncoder, memberMapper, memberService, JWT_HEADER, JWT_HEADER_PREFIX);
    }

    @Test
    void list() {
        // empty
        CommonPage<CartProductDTO> result = cartProductService.list(1L, 20L);
        assertEquals(0, result.getData().size());

        // exist
        Product product = new Product();
        product.setIsPublish(Product.PublishStatus.PUBLISH.getValue());
        productMapper.insert(product);
        List<Sku> skus = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Sku sku = new Sku();
            sku.setProductId(product.getId());
            sku.setPrice(new BigDecimal(getRandomNum(3)));
            skuMapper.insert(sku);
            sku = skuMapper.selectById(sku.getId());
            skus.add(sku);
        }
        for (Sku value : skus) {
            CartProduct cartProduct = new CartProduct();
            cartProduct.setProductId(product.getId());
            cartProduct.setMemberId(member.getId());
            cartProduct.setSkuId(value.getId());
            cartProductMapper.insert(cartProduct);
        }
        CartProduct cartProduct = new CartProduct();
        cartProduct.setProductId(product.getId());
        cartProduct.setMemberId(member.getId());
        cartProduct.setSkuId(skus.get(skus.size() - 1).getId() + 1L);
        cartProductMapper.insert(cartProduct);
        CommonPage<CartProductDTO> result1 = cartProductService.list(1L, 20L);
        assertEquals(skus.size() + 1, result1.getData().size());
        for (CartProductDTO cartProductDTO : result1.getData()) {
            assertEquals(Product.PublishStatus.PUBLISH.getValue(), cartProductDTO.getIsPublish());
            if (cartProductDTO.getSkuId().equals(cartProduct.getSkuId())) {
                assertEquals(CartProductDTO.SKU_NOT_EXIST, cartProductDTO.getIsSkuExist());
            } else {
                assertEquals(CartProductDTO.SKU_EXIST, cartProductDTO.getIsSkuExist());
                Sku compare = null;
                for (Sku sku : skus) {
                    if (sku.getId().equals(cartProductDTO.getSkuId())) {
                        compare = sku;
                        break;
                    }
                }
                assertNotNull(compare);
                assertEquals(compare.getPrice(), cartProductDTO.getPrice());
            }
        }
    }

    @Test
    void add() {
        // legal
        Product product = new Product();
        product.setBrandId(1L);
        product.setProductCategoryId(1L);
        product.setName(getRandomString(5));
        product.setBrandName(getRandomString(5));
        product.setProductCategoryName(getRandomString(5));
        product.setProductSn(getRandomString(20));
        productMapper.insert(product);
        Sku sku = new Sku();
        sku.setProductId(product.getId());
        sku.setSkuCode(getRandomString(20));
        sku.setPic(getRandomString(100));
        sku.setSpecification(getRandomString(50));
        skuMapper.insert(sku);
        AddCartProductParam param = new AddCartProductParam();
        param.setProductId(product.getId());
        param.setSkuId(sku.getId());
        param.setQuantity(10);
        cartProductService.add(param);
        CartProduct tmp = cartProductMapper.selectQuantityByMemberIdAndSkuId(member.getId(), sku.getId());
        CartProduct cartProduct = cartProductMapper.selectById(tmp.getId());
        assertNotNull(cartProduct);
        assertEquals(product.getBrandId(), cartProduct.getBrandId());
        assertEquals(product.getProductCategoryId(), cartProduct.getProductCategoryId());
        assertEquals(product.getName(), cartProduct.getName());
        assertEquals(product.getBrandName(), cartProduct.getBrandName());
        assertEquals(product.getProductCategoryName(), cartProduct.getProductCategoryName());
        assertEquals(product.getProductSn(), cartProduct.getProductSn());
        assertEquals(sku.getSkuCode(), cartProduct.getSkuCode());
        assertEquals(sku.getPic(), cartProduct.getSkuPic());
        assertEquals(sku.getSpecification(), cartProduct.getSpecification());
        assertEquals(member.getId(), cartProduct.getMemberId());
        assertEquals(member.getUsername(), cartProduct.getMemberUsername());
        // 增加数量
        AddCartProductParam param4 = new AddCartProductParam();
        param4.setProductId(product.getId());
        param4.setSkuId(sku.getId());
        param4.setQuantity(10);
        cartProductService.add(param4);
        CartProduct update = cartProductMapper.selectById(cartProduct.getId());
        assertEquals(cartProduct.getQuantity() + 10, update.getQuantity());

        // 商品不存在
        AddCartProductParam param1 = new AddCartProductParam();
        param1.setProductId(-1L);
        param1.setSkuId(sku.getId());
        param1.setQuantity(10);
        assertThrows(IllegalArgumentException.class, () -> cartProductService.add(param1));
        // sku不合法
        AddCartProductParam param2 = new AddCartProductParam();
        param2.setProductId(product.getId());
        param2.setSkuId(-1L);
        param2.setQuantity(10);
        assertThrows(IllegalArgumentException.class, () -> cartProductService.add(param2));
        // 商品数量不合法
        AddCartProductParam param3 = new AddCartProductParam();
        param3.setProductId(product.getId());
        param3.setSkuId(sku.getId());
        param3.setQuantity(0);
        assertThrows(IllegalArgumentException.class, () -> cartProductService.add(param3));
    }

    @Test
    void updateQuantity() {
        // legal
        CartProduct cartProduct = new CartProduct();
        cartProduct.setMemberId(member.getId());
        cartProduct.setQuantity(10);
        cartProductMapper.insert(cartProduct);
        cartProductService.updateQuantity(cartProduct.getId(), 2);
        cartProduct = cartProductMapper.selectById(cartProduct.getId());
        assertEquals(2, cartProduct.getQuantity());

        // illegal
        assertThrows(IllegalArgumentException.class, () -> cartProductService.updateQuantity(-1L, 2));
        CartProduct finalCartProduct = cartProduct;
        assertThrows(IllegalArgumentException.class, () -> cartProductService.updateQuantity(finalCartProduct.getId(), 0));
    }

    @Test
    void updateSku() {
        // legal
        Long productId = 1L;
        Sku sku = new Sku();
        sku.setProductId(productId);
        sku.setSkuCode(getRandomString(20));
        sku.setPic(getRandomString(50));
        sku.setSpecification(getRandomString(20));
        skuMapper.insert(sku);
        CartProduct cartProduct = new CartProduct();
        cartProduct.setMemberId(member.getId());
        cartProduct.setProductId(productId);
        cartProduct.setSkuId(1L);
        cartProductMapper.insert(cartProduct);
        cartProductService.updateSku(cartProduct.getId(), sku.getId());
        CartProduct update = cartProductMapper.selectById(cartProduct.getId());
        assertEquals(sku.getId(), update.getSkuId());
        assertEquals(sku.getSkuCode(), update.getSkuCode());
        assertEquals(sku.getPic(), update.getSkuPic());
        assertEquals(sku.getSpecification(), update.getSpecification());

        // illegal
        Sku sku1 = new Sku();
        sku1.setProductId(2L);
        skuMapper.insert(sku1);
        assertThrows(IllegalArgumentException.class, () -> cartProductService.updateSku(-1L, sku.getId()));
        assertThrows(IllegalArgumentException.class, () -> cartProductService.updateSku(cartProduct.getId(), -1L));
        assertThrows(IllegalArgumentException.class, () -> cartProductService.updateSku(cartProduct.getId(), sku1.getId()));
    }

    @Test
    void delete() {
        List<Long> ids = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CartProduct cartProduct = new CartProduct();
            cartProduct.setMemberId(member.getId());
            cartProductMapper.insert(cartProduct);
            ids.add(cartProduct.getId());
        }
        ids.add(-1L);
        cartProductService.delete(ids);
        for (Long id : ids) {
            assertNull(cartProductMapper.selectById(id));
        }
    }
}