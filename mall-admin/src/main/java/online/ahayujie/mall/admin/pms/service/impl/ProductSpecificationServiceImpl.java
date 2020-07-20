package online.ahayujie.mall.admin.pms.service.impl;

import online.ahayujie.mall.admin.pms.bean.dto.ProductDTO;
import online.ahayujie.mall.admin.pms.bean.model.ProductSpecification;
import online.ahayujie.mall.admin.pms.bean.model.ProductSpecificationValue;
import online.ahayujie.mall.admin.pms.exception.IllegalProductSpecificationException;
import online.ahayujie.mall.admin.pms.mapper.ProductSpecificationMapper;
import online.ahayujie.mall.admin.pms.mapper.ProductSpecificationValueMapper;
import online.ahayujie.mall.admin.pms.service.ProductSpecificationService;
import online.ahayujie.mall.admin.pms.service.SpecificationValueStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品规格 服务实现类
 * </p>
 *
 * @author aha
 * @since 2020-07-14
 */
@Service
public class ProductSpecificationServiceImpl implements ProductSpecificationService {
    private SpecificationValueStrategyFactory specificationValueStrategyFactory;

    private final ProductSpecificationMapper productSpecificationMapper;
    private final ProductSpecificationValueMapper productSpecificationValueMapper;

    public ProductSpecificationServiceImpl(ProductSpecificationMapper productSpecificationMapper,
                                           ProductSpecificationValueMapper productSpecificationValueMapper) {
        this.productSpecificationMapper = productSpecificationMapper;
        this.productSpecificationValueMapper = productSpecificationValueMapper;
    }

    @Override
    public void validate(ProductSpecificationValue specificationValue) throws IllegalProductSpecificationException {
        ProductSpecificationValue.Type[] types = ProductSpecificationValue.Type.values();
        Integer type = specificationValue.getType();
        if (type != null && !Arrays.stream(types)
                .map(ProductSpecificationValue.Type::getValue).collect(Collectors.toList())
                .contains(type)) {
            throw new IllegalProductSpecificationException("选项类型不存在：" + type);
        }
        String value = specificationValue.getValue();
        if (type != null && value != null) {
            SpecificationValueStrategy strategy = specificationValueStrategyFactory.getStrategy(type);
            strategy.validate(value);
        }
    }

    @Override
    public List<ProductSpecification> saveSpecifications(List<ProductSpecification> productSpecifications) {
        productSpecificationMapper.insertList(productSpecifications);
        return productSpecifications;
    }

    @Override
    public List<ProductSpecificationValue> saveSpecificationValues(List<ProductSpecificationValue> specificationValues) {
        productSpecificationValueMapper.insertList(specificationValues);
        return specificationValues;
    }

    @Override
    public List<ProductDTO.SpecificationDTO> getByProductId(Long productId) {
        List<ProductDTO.SpecificationDTO> specificationDTOS = productSpecificationMapper.selectDTOByProductId(productId);
        if (CollectionUtils.isEmpty(specificationDTOS)) {
            return null;
        }
        return specificationDTOS;
    }

    @Autowired
    public void setSpecificationValueStrategyFactory(SpecificationValueStrategyFactory specificationValueStrategyFactory) {
        this.specificationValueStrategyFactory = specificationValueStrategyFactory;
    }
}
