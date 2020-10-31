package online.ahayujie.mall.search.service;

/**
 * 商品规格选项值策略接口。
 * 负责检查解析不同类型的规格选项值。
 *
 * @author aha
 * @since 2020/10/28
 */
public interface SpecificationValueStrategy {
    /**
     * 从json类型的value中解析获取文本类型的value。
     * 解析失败则返回null。
     *
     * @param value json类型的value
     * @return 文本类型的value
     */
    String getTextValue(String value);
}
