package online.ahayujie.mall.search.service.impl;

import online.ahayujie.mall.search.bean.model.ProductSpecificationValue;
import online.ahayujie.mall.search.service.SpecificationValueStrategy;
import online.ahayujie.mall.search.service.SpecificationValueStrategyFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author aha
 * @since 2020/10/28
 */
@Service
public class SpecificationValueStrategyFactoryImpl implements SpecificationValueStrategyFactory {
    private final Map<String, SpecificationValueStrategy> strategyMap;

    public SpecificationValueStrategyFactoryImpl(Map<String, SpecificationValueStrategy> strategyMap) {
        this.strategyMap = strategyMap;
    }

    @Override
    public SpecificationValueStrategy getStrategy(ProductSpecificationValue.Type type) {
        if (type == null) {
            return null;
        }
        return getStrategy(type.getName());
    }

    @Override
    public SpecificationValueStrategy getStrategy(Integer value) {
        for (ProductSpecificationValue.Type existType : ProductSpecificationValue.Type.values()) {
            if (existType.getValue().equals(value)) {
                return getStrategy(existType.getName());
            }
        }
        return null;
    }

    @Override
    public SpecificationValueStrategy getStrategy(String name) {
        return strategyMap.getOrDefault(name, null);
    }
}
