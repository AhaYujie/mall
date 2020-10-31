package online.ahayujie.mall.search.service;

import online.ahayujie.mall.search.bean.model.ProductSpecificationValue;

/**
 * 商品规格选项值策略工厂
 * @author aha
 * @since 2020/10/28
 */
public interface SpecificationValueStrategyFactory {
    /**
     * 根据 {@link ProductSpecificationValue.Type} 获取策略实现类。
     * 如果不存在则返回null。
     *
     * @param type 商品规格选项类型
     * @return 策略实现类
     */
    SpecificationValueStrategy getStrategy(ProductSpecificationValue.Type type);

    /**
     * 根据 {@link ProductSpecificationValue.Type#getValue()} 获取策略实现类。
     * 如果不存在则返回null。
     *
     * @param value 商品规格选项类型值
     * @return 策略实现类
     */
    SpecificationValueStrategy getStrategy(Integer value);

    /**
     * 根据 {@link ProductSpecificationValue.Type#getValue()} 获取策略实现类。
     * 如果不存在则返回null。
     *
     * @param name 商品规格选项类型名称
     * @return 策略实现类
     */
    SpecificationValueStrategy getStrategy(String name);
}
