package online.ahayujie.mall.admin.pms.service.impl;

import online.ahayujie.mall.admin.pms.bean.model.ProductSpecificationValue;
import online.ahayujie.mall.admin.pms.service.SpecificationValueStrategy;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 商品规格选项值策略工厂
 * @author aha
 * @since 2020/7/15
 */
@Service
public class SpecificationValueStrategyFactory {
    private final Map<String, SpecificationValueStrategy> strategyMap;

    public SpecificationValueStrategyFactory(Map<String, SpecificationValueStrategy> strategyMap) {
        this.strategyMap = strategyMap;
    }

    /**
     * 根据 {@link online.ahayujie.mall.admin.pms.bean.model.ProductSpecificationValue.Type}
     * 的 {@code name} 获取策略实现类
     * @param typeName 商品规格选项类型名称
     * @return 策略实现类
     */
    public SpecificationValueStrategy getStrategy(String typeName) {
        return strategyMap.get(typeName);
    }

    /**
     * 根据 {@link online.ahayujie.mall.admin.pms.bean.model.ProductSpecificationValue.Type}
     * 的 {@code value} 获取策略实现类
     * @param typeValue 商品规格选项类型值
     * @return 策略实现类
     */
    public SpecificationValueStrategy getStrategy(Integer typeValue) {
        for (ProductSpecificationValue.Type existType : ProductSpecificationValue.Type.values()) {
            if (existType.getValue().equals(typeValue)) {
                return getStrategy(existType.getName());
            }
        }
        return null;
    }
}
