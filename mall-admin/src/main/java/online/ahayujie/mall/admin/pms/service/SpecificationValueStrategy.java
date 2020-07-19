package online.ahayujie.mall.admin.pms.service;

import online.ahayujie.mall.admin.pms.exception.IllegalProductSpecificationException;

/**
 * 商品规格选项值策略接口。
 * 负责检查解析不同类型的规格选项值
 * @author aha
 * @since 2020/7/15
 */
public interface SpecificationValueStrategy {
    /**
     * 检查商品规格选项值是否合法
     * @param value 商品规格选项值
     * @throws IllegalProductSpecificationException 商品规格选项值不合法
     */
    void validate(String value) throws IllegalProductSpecificationException;

    /**
     * 获取文本形式的商品规格选项值
     * @param value 商品规格选项值
     * @return 文本形式的商品规格选项值
     * @throws IllegalProductSpecificationException 商品规格选项值不合法
     */
    String getText(String value) throws IllegalProductSpecificationException;
}
