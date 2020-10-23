package online.ahayujie.mall.portal.oms.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.portal.mms.bean.model.Member;
import online.ahayujie.mall.portal.mms.service.MemberService;
import online.ahayujie.mall.portal.oms.bean.dto.AddCartProductParam;
import online.ahayujie.mall.portal.oms.bean.dto.CartProductDTO;
import online.ahayujie.mall.portal.oms.bean.model.CartProduct;
import online.ahayujie.mall.portal.oms.mapper.CartProductMapper;
import online.ahayujie.mall.portal.oms.service.CartProductService;
import online.ahayujie.mall.portal.pms.bean.model.Product;
import online.ahayujie.mall.portal.pms.bean.model.Sku;
import online.ahayujie.mall.portal.pms.service.ProductService;
import online.ahayujie.mall.portal.pms.service.SkuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 购物车商品表 服务实现类
 * </p>
 *
 * @author aha
 * @since 2020-10-22
 */
@Service
public class CartProductServiceImpl implements CartProductService {
    private SkuService skuService;
    private MemberService memberService;
    private ProductService productService;

    private final CartProductMapper cartProductMapper;

    public CartProductServiceImpl(CartProductMapper cartProductMapper) {
        this.cartProductMapper = cartProductMapper;
    }

    @Override
    public CommonPage<CartProductDTO> list(Long pageNum, Long pageSize) {
        Member member = memberService.getMemberFromToken();
        Page<CartProductDTO> page = new Page<>(pageNum, pageSize);
        Page<CartProductDTO> cartProductDTOPage = cartProductMapper.selectPageByMemberId(page, member.getId());
        List<Long> productIds = cartProductDTOPage.getRecords().stream().map(CartProductDTO::getProductId).collect(Collectors.toList());
        Map<Long, Integer> isPublishMap = productService.getIsPublish(productIds);
        List<Long> skuIds = cartProductDTOPage.getRecords().stream().map(CartProductDTO::getSkuId).collect(Collectors.toList());
        Map<Long, BigDecimal> priceMap = skuService.getPrice(skuIds);
        for (CartProductDTO cartProduct : cartProductDTOPage.getRecords()) {
            cartProduct.setIsPublish(isPublishMap.getOrDefault(cartProduct.getProductId(), Product.PublishStatus.NOT_PUBLISH.getValue()));
            BigDecimal price = priceMap.get(cartProduct.getSkuId());
            cartProduct.setPrice(price);
            cartProduct.setIsSkuExist(price == null ? CartProductDTO.SKU_NOT_EXIST : CartProductDTO.SKU_EXIST);
        }
        return new CommonPage<>(cartProductDTOPage);
    }

    @Override
    public void add(AddCartProductParam param) throws IllegalArgumentException {
        Product product = productService.getById(param.getProductId());
        if (product == null) {
            throw new IllegalArgumentException("商品不存在");
        }
        Sku sku = skuService.getById(param.getSkuId());
        if (sku == null || !sku.getProductId().equals(product.getId())) {
            throw new IllegalArgumentException("sku不合法");
        }
        if (param.getQuantity() <= 0) {
            throw new IllegalArgumentException("商品数量不合法");
        }
        Member member = memberService.getMemberFromToken();
        CartProduct cartProduct = cartProductMapper.selectQuantityByMemberIdAndSkuId(member.getId(), param.getSkuId());
        if (cartProduct != null) {
            cartProduct.setQuantity(cartProduct.getQuantity() + param.getQuantity());
            cartProduct.setUpdateTime(new Date());
            cartProductMapper.updateById(cartProduct);
        } else {
            cartProduct = new CartProduct();
            cartProduct.setProductId(param.getProductId());
            cartProduct.setSkuId(param.getSkuId());
            cartProduct.setQuantity(param.getQuantity());
            BeanUtils.copyProperties(product, cartProduct);
            BeanUtils.copyProperties(sku, cartProduct);
            cartProduct.setSkuPic(sku.getPic());
            cartProduct.setMemberId(member.getId());
            cartProduct.setMemberUsername(member.getUsername());
            cartProduct.setId(null);
            cartProduct.setUpdateTime(null);
            cartProduct.setCreateTime(new Date());
            cartProductMapper.insert(cartProduct);
        }
    }

    @Override
    public void updateQuantity(Long id, Integer quantity) throws IllegalArgumentException {
        if (quantity <= 0) {
            throw new IllegalArgumentException("商品数量不合法");
        }
        Member member = memberService.getMemberFromToken();
        CartProduct cartProduct = cartProductMapper.selectByIdAndMemberId(id, member.getId());
        if (cartProduct == null) {
            throw new IllegalArgumentException("购物车中不存在该商品");
        }
        CartProduct update = new CartProduct();
        update.setId(id);
        update.setQuantity(quantity);
        update.setUpdateTime(new Date());
        cartProductMapper.updateById(update);
    }

    @Override
    public void updateSku(Long cartProductId, Long skuId) throws IllegalArgumentException {
        Member member = memberService.getMemberFromToken();
        CartProduct cartProduct = cartProductMapper.selectByIdAndMemberId(cartProductId, member.getId());
        if (cartProduct == null) {
            throw new IllegalArgumentException("购物车中不存在该商品");
        }
        Sku sku = skuService.getById(skuId);
        if (sku == null || !cartProduct.getProductId().equals(sku.getProductId())) {
            throw new IllegalArgumentException("sku不属于该商品");
        }
        CartProduct update = new CartProduct();
        update.setId(cartProductId);
        update.setSkuId(skuId);
        update.setSkuCode(sku.getSkuCode());
        update.setSkuPic(sku.getPic());
        update.setSpecification(sku.getSpecification());
        update.setUpdateTime(new Date());
        cartProductMapper.updateById(update);
    }

    @Override
    public void delete(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        Member member = memberService.getMemberFromToken();
        cartProductMapper.deleteByIdAndMemberId(ids, member.getId());
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
}
