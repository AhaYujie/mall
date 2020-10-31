package online.ahayujie.mall.search.service;

import online.ahayujie.mall.search.bean.model.ProductParam;

/**
 * 商品参数值策略工厂
 * @author aha
 * @since 2020/10/27
 */
public interface ProductParamValueStrategyFactory {
    /**
     * 根据 {@link ProductParam.Type} 获取商品参数值策略。
     * 不存在则返回null。
     *
     * @param type 商品参数值类型
     * @return 商品参数值策略
     */
    ProductParamValueStrategy getStrategy(ProductParam.Type type);

    /**
     * 根据 {@link ProductParam.Type#getName()} 获取商品参数值策略。
     * 不存在则返回null。
     *
     * @param typeName 商品参数值类型名称
     * @return 商品参数值策略
     */
    ProductParamValueStrategy getStrategy(String typeName);

    /**
     * 根据 {@link ProductParam.Type#getValue()} ()} 获取商品参数值策略。
     * 不存在则返回null。
     *
     * @param typeValue 商品参数值类型值
     * @return 商品参数值策略
     */
    ProductParamValueStrategy getStrategy(Integer typeValue);
}
