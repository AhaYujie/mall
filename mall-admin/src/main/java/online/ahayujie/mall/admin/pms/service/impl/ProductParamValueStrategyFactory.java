package online.ahayujie.mall.admin.pms.service.impl;

import online.ahayujie.mall.admin.pms.bean.model.ProductParam;
import online.ahayujie.mall.admin.pms.service.ProductParamValueStrategy;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 商品参数值策略工厂
 * @author aha
 * @since 2020/7/15
 */
@Service
public class ProductParamValueStrategyFactory {
    private final Map<String, ProductParamValueStrategy> strategyMap;

    public ProductParamValueStrategyFactory(Map<String, ProductParamValueStrategy> strategyMap) {
        this.strategyMap = strategyMap;
    }

    /**
     * 根据 {@link online.ahayujie.mall.admin.pms.bean.model.ProductParam.Type}
     * 的 {@code name} 获取策略
     * @param typeName 商品参数值类型名称
     * @return 策略
     */
    public ProductParamValueStrategy getStrategy(String typeName) {
        return strategyMap.get(typeName);
    }

    /**
     * 根据 {@link online.ahayujie.mall.admin.pms.bean.model.ProductParam.Type}
     * 的 {@code value} 获取策略
     * @param typeValue 商品参数值类型值
     * @return 策略
     */
    public ProductParamValueStrategy getStrategy(Integer typeValue) {
        for (ProductParam.Type existType : ProductParam.Type.values()) {
            if (existType.getValue().equals(typeValue)) {
                return getStrategy(existType.getName());
            }
        }
        return null;
    }
}
