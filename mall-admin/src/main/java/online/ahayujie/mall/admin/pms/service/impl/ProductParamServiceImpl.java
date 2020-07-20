package online.ahayujie.mall.admin.pms.service.impl;

import online.ahayujie.mall.admin.pms.bean.model.ProductParam;
import online.ahayujie.mall.admin.pms.exception.IllegalProductParamException;
import online.ahayujie.mall.admin.pms.mapper.ProductParamMapper;
import online.ahayujie.mall.admin.pms.service.ProductParamService;
import online.ahayujie.mall.admin.pms.service.ProductParamValueStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品参数 服务实现类
 * </p>
 *
 * @author aha
 * @since 2020-07-14
 */
@Service
public class ProductParamServiceImpl implements ProductParamService {
    private ProductParamValueStrategyFactory productParamValueStrategyFactory;

    private final ProductParamMapper productParamMapper;

    public ProductParamServiceImpl(ProductParamMapper productParamMapper) {
        this.productParamMapper = productParamMapper;
    }

    @Override
    public void validate(ProductParam productParam) throws IllegalProductParamException {
        ProductParam.Type[] types = ProductParam.Type.values();
        Integer type = productParam.getType();
        if (type != null && !Arrays.stream(types)
                .map(ProductParam.Type::getValue).collect(Collectors.toList())
                .contains(type)) {
            throw new IllegalProductParamException("商品参数值类型不合法：" + type);
        }
        String value = productParam.getValue();
        if (type != null && value != null) {
            ProductParamValueStrategy strategy = productParamValueStrategyFactory.getStrategy(type);
            strategy.validate(value);
        }
    }

    @Override
    public List<ProductParam> save(List<ProductParam> productParams) {
        productParamMapper.insertList(productParams);
        return productParams;
    }

    @Override
    public List<ProductParam> getByProductId(Long productId) {
        List<ProductParam> productParams = productParamMapper.selectByProductId(productId);
        if (CollectionUtils.isEmpty(productParams)) {
            return null;
        }
        return productParams;
    }

    @Autowired
    public void setProductParamValueStrategyFactory(ProductParamValueStrategyFactory productParamValueStrategyFactory) {
        this.productParamValueStrategyFactory = productParamValueStrategyFactory;
    }
}
