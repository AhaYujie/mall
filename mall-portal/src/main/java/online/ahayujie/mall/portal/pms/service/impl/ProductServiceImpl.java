package online.ahayujie.mall.portal.pms.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.portal.pms.bean.dto.*;
import online.ahayujie.mall.portal.pms.bean.model.Product;
import online.ahayujie.mall.portal.pms.mapper.*;
import online.ahayujie.mall.portal.pms.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品信息 服务实现类
 * </p>
 *
 * @author aha
 * @since 2020-10-17
 */
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    @Value("${mall-search.search-url}")
    private String MALL_SEARCH_URL;
    @Value("${mall-search.recommend-url}")
    private String MALL_RECOMMEND_URL;

    private final SkuMapper skuMapper;
    private final RestTemplate restTemplate;
    private final ProductMapper productMapper;
    private final SkuImageMapper skuImageMapper;
    private final ProductImageMapper productImageMapper;
    private final ProductParamMapper productParamMapper;
    private final ProductSpecificationMapper productSpecificationMapper;

    public ProductServiceImpl(SkuMapper skuMapper, RestTemplate restTemplate, ProductMapper productMapper,
                              SkuImageMapper skuImageMapper, ProductImageMapper productImageMapper,
                              ProductParamMapper productParamMapper, ProductSpecificationMapper productSpecificationMapper) {
        this.skuMapper = skuMapper;
        this.restTemplate = restTemplate;
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

    @Override
    public CommonPage<ProductDTO> search(SearchProductParam param) {
        QueryEsProductParam queryParam = new QueryEsProductParam();
        BeanUtils.copyProperties(param, queryParam);
        queryParam.setIsPublish(Product.PublishStatus.PUBLISH.getValue());
        QueryEsProductDTO response;
        try {
            response = restTemplate.postForObject(MALL_SEARCH_URL, queryParam, QueryEsProductDTO.class);
        } catch (RestClientException e) {
            log.error(e.getMessage());
            throw e;
        }
        if (response == null || response.getData() == null) {
            log.error("搜索商品无结果: {}", response);
            return null;
        }
        return getProductDTOPage(response.getData());
    }

    @Override
    public CommonPage<ProductDTO> recommend(RecommendProductParam param) {
        if (productMapper.selectIsPublish(param.getId()) == null) {
            return null;
        }
        RecommendEsProductDTO response;
        try {
            response = restTemplate.postForObject(MALL_RECOMMEND_URL, param, RecommendEsProductDTO.class);
        } catch (RestClientException e) {
            log.error(e.getMessage());
            throw e;
        }
        if (response == null || response.getData() == null) {
            log.error("推荐商品无结果: {}", response);
            return null;
        }
        return getProductDTOPage(response.getData());
    }

    private CommonPage<ProductDTO> getProductDTOPage(CommonPage<EsProduct> page) {
        List<ProductDTO> productDTOS = page.getData().stream().map(esProduct -> {
            ProductDTO productDTO = new ProductDTO();
            BeanUtils.copyProperties(esProduct, productDTO);
            return productDTO;
        }).collect(Collectors.toList());
        return new CommonPage<>(page.getPageNum(), page.getPageSize(), page.getTotalPage(), page.getTotal(), productDTOS);
    }
}
