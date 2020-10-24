package online.ahayujie.mall.portal.pms.service.impl;

import online.ahayujie.mall.portal.pms.bean.dto.ProductDetailDTO;
import online.ahayujie.mall.portal.pms.bean.dto.SkuDTO;
import online.ahayujie.mall.portal.pms.bean.model.Product;
import online.ahayujie.mall.portal.pms.mapper.*;
import online.ahayujie.mall.portal.pms.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品信息 服务实现类
 * </p>
 *
 * @author aha
 * @since 2020-10-17
 */
@Service
public class ProductServiceImpl implements ProductService {
    private final SkuMapper skuMapper;
    private final ProductMapper productMapper;
    private final SkuImageMapper skuImageMapper;
    private final ProductImageMapper productImageMapper;
    private final ProductParamMapper productParamMapper;
    private final ProductSpecificationMapper productSpecificationMapper;

    public ProductServiceImpl(SkuMapper skuMapper, ProductMapper productMapper, SkuImageMapper skuImageMapper,
                              ProductImageMapper productImageMapper, ProductParamMapper productParamMapper,
                              ProductSpecificationMapper productSpecificationMapper) {
        this.skuMapper = skuMapper;
        this.productMapper = productMapper;
        this.skuImageMapper = skuImageMapper;
        this.productImageMapper = productImageMapper;
        this.productParamMapper = productParamMapper;
        this.productSpecificationMapper = productSpecificationMapper;
    }

    @Override
    public ProductDetailDTO getDetail(Long id, Integer isMobile) {
        ProductDetailDTO.ProductInfo productInfo = null;
        if (isMobile == 0) {
            productInfo = productMapper.selectDetail(id, Product.PublishStatus.PUBLISH.getValue());
        } else if (isMobile == 1) {
            productInfo = productMapper.selectMobileDetail(id, Product.PublishStatus.PUBLISH.getValue());
        }
        if (productInfo == null) {
            return null;
        }
        List<String> productImages = productImageMapper.selectDetailImages(id);
        List<ProductDetailDTO.Param> params = productParamMapper.selectDetailParam(id);
        List<ProductDetailDTO.Specification> specifications = productSpecificationMapper.selectDetailSpecification(id);
        List<ProductDetailDTO.Sku> skus = skuMapper.selectDetailSku(id);
        return new ProductDetailDTO(productInfo, productImages, params, specifications, skus);
    }

    @Override
    public List<String> getSkuImages(Long skuId) {
        Long productId = skuMapper.selectProductId(skuId);
        Product product = productMapper.selectIsPublish(productId);
        if (product == null || !Product.PublishStatus.PUBLISH.getValue().equals(product.getIsPublish())) {
            return null;
        }
        return skuImageMapper.selectBySkuId(skuId);
    }

    @Override
    public Map<Long, Integer> getIsPublish(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }
        List<Product> products = productMapper.selectIsPublishBatch(ids);
        Map<Long, Integer> map = new HashMap<>();
        for (Product product : products) {
            map.put(product.getId(), product.getIsPublish());
        }
        return map;
    }

    @Override
    public Product getById(Long id) {
        return productMapper.selectByIdAndIsPublish(id, Product.PublishStatus.PUBLISH.getValue());
    }

    @Override
    public SkuDTO getSku(Long id) {
        Product product = productMapper.selectIsPublish(id);
        if (product == null || !Product.PublishStatus.PUBLISH.getValue().equals(product.getIsPublish())) {
            return null;
        }
        List<ProductDetailDTO.Specification> specifications = productSpecificationMapper.selectDetailSpecification(id);
        List<ProductDetailDTO.Sku> skus = skuMapper.selectDetailSku(id);
        return new SkuDTO(specifications, skus);
    }
}
