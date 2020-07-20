package online.ahayujie.mall.admin.pms.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.pms.bean.dto.ProductDTO;
import online.ahayujie.mall.admin.pms.bean.dto.ProductSpecificationDTO;
import online.ahayujie.mall.admin.pms.bean.model.Sku;
import online.ahayujie.mall.admin.pms.bean.model.SkuImage;
import online.ahayujie.mall.admin.pms.bean.model.SkuSpecificationRelationship;
import online.ahayujie.mall.admin.pms.exception.IllegalSkuException;
import online.ahayujie.mall.admin.pms.mapper.SkuImageMapper;
import online.ahayujie.mall.admin.pms.mapper.SkuMapper;
import online.ahayujie.mall.admin.pms.mapper.SkuSpecificationRelationshipMapper;
import online.ahayujie.mall.admin.pms.service.SkuService;
import online.ahayujie.mall.admin.pms.service.SpecificationValueStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品sku 服务实现类
 * </p>
 *
 * @author aha
 * @since 2020-07-14
 */
@Slf4j
@Service
public class SkuServiceImpl implements SkuService {
    private SpecificationValueStrategyFactory specificationValueStrategyFactory;

    private final SkuMapper skuMapper;
    private final ObjectMapper objectMapper;
    private final SkuImageMapper skuImageMapper;
    private final SkuSpecificationRelationshipMapper skuSpecificationRelationshipMapper;

    public SkuServiceImpl(SkuMapper skuMapper, ObjectMapper objectMapper, SkuImageMapper skuImageMapper,
                          SkuSpecificationRelationshipMapper skuSpecificationRelationshipMapper) {
        this.skuMapper = skuMapper;
        this.objectMapper = objectMapper;
        this.skuImageMapper = skuImageMapper;
        this.skuSpecificationRelationshipMapper = skuSpecificationRelationshipMapper;
    }

    @Override
    public void validate(Sku sku) throws IllegalSkuException {
        BigDecimal price = sku.getPrice();
        if (price != null && price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalSkuException("价格小于0：" + price);
        }
        BigDecimal promotionPrice = sku.getPromotionPrice();
        if (promotionPrice != null && promotionPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalSkuException("促销价格小于0：" + promotionPrice);
        }
        Integer stock = sku.getStock();
        if (stock != null && stock.compareTo(0) < 0) {
            throw new IllegalSkuException("库存小于0：" + stock);
        }
        Integer lowStock = sku.getLowStock();
        if (lowStock != null && lowStock.compareTo(0) < 0) {
            throw new IllegalSkuException("预警库存小于0：" + lowStock);
        }
        Integer sale = sku.getSale();
        if (sale != null && sale.compareTo(0) < 0) {
            throw new IllegalSkuException("销量小于0：" + sale);
        }
    }

    @Override
    public String generateSkuCode(Sku sku, Integer index) {
        Random random = new Random();
        Long productId = sku.getProductId();
        if (productId == null) {
            throw new NullPointerException();
        }
        StringBuilder stringBuilder = new StringBuilder();
        // 时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        stringBuilder.append(simpleDateFormat.format(new Date()));
        // 2位随机数
        String towRandom = String.format("%02d", random.nextInt(100));
        stringBuilder.append(towRandom);
        // 商品id
        String productIdStr = productId.toString();
        if (productIdStr.length() < 6) {
            productIdStr = String.format("%06d", productId);
        }
        stringBuilder.append(productIdStr);
        // 3位index
        if (index != null) {
            String indexStr = String.format("%03d", index);
            if (indexStr.length() > 3) {
                indexStr = indexStr.substring(indexStr.length() - 3);
            }
            stringBuilder.append(indexStr);
        } else {
            String threeRandom = String.format("%03d", random.nextInt(1000));
            stringBuilder.append(threeRandom);
        }
        return stringBuilder.toString();
    }

    @Override
    public String generateSpecification(List<ProductSpecificationDTO> specificationDTOS) {
        List<SpecificationItemJson> itemJsons = specificationDTOS.stream()
                .map(specificationDTO -> {
                    SpecificationItemJson itemJson = new SpecificationItemJson();
                    itemJson.setKey(specificationDTO.getSpecification().getName());
                    SpecificationValueStrategy strategy = specificationValueStrategyFactory
                            .getStrategy(specificationDTO.getSpecificationValue().getType());
                    itemJson.setValue(strategy.getText(specificationDTO.getSpecificationValue().getValue()));
                    return itemJson;
                }).collect(Collectors.toList());
        try {
            return objectMapper.writeValueAsString(itemJsons);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @Override
    public void saveSkuImages(List<SkuImage> skuImages) {
        if (CollectionUtils.isEmpty(skuImages)) {
            return;
        }
        skuImageMapper.insertList(skuImages);
    }

    @Override
    public void saveSkuSpecificationRelationships(List<SkuSpecificationRelationship> relationships) {
        if (CollectionUtils.isEmpty(relationships)) {
            return;
        }
        skuSpecificationRelationshipMapper.insertList(relationships);
    }

    @Override
    public List<ProductDTO.SkuDTO> getByProductId(Long productId) {
        List<ProductDTO.SkuDTO> skuDTOList = skuMapper.selectDTOByProductId(productId);
        if (CollectionUtils.isEmpty(skuDTOList)) {
            return null;
        }
        return skuDTOList;
    }

    @Data
    private static class SpecificationItemJson {
        private String key;
        private String value;
    }

    @Autowired
    public void setSpecificationValueStrategyFactory(SpecificationValueStrategyFactory specificationValueStrategyFactory) {
        this.specificationValueStrategyFactory = specificationValueStrategyFactory;
    }
}
