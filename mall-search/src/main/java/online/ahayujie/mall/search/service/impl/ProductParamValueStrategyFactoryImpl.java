package online.ahayujie.mall.search.service.impl;

import online.ahayujie.mall.search.bean.model.ProductParam;
import online.ahayujie.mall.search.service.ProductParamValueStrategy;
import online.ahayujie.mall.search.service.ProductParamValueStrategyFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author aha
 * @since 2020/10/27
 */
@Service
public class ProductParamValueStrategyFactoryImpl implements ProductParamValueStrategyFactory {
    private final Map<String, ProductParamValueStrategy> strategyMap;

    public ProductParamValueStrategyFactoryImpl(Map<String, ProductParamValueStrategy> strategyMap) {
        this.strategyMap = strategyMap;
    }

    @Override
    public ProductParamValueStrategy getStrategy(ProductParam.Type type) {
        if (type == null) {
            return null;
        }
        return getStrategy(type.getName());
    }

    @Override
    public ProductParamValueStrategy getStrategy(String typeName) {
        return strategyMap.getOrDefault(typeName, null);
    }

    @Override
    public ProductParamValueStrategy getStrategy(Integer typeValue) {
        for (ProductParam.Type existType : ProductParam.Type.values()) {
            if (existType.getValue().equals(typeValue)) {
                return getStrategy(existType);
            }
        }
        return null;
    }
}
