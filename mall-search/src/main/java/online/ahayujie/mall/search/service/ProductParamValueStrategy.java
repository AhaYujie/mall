package online.ahayujie.mall.search.service;

/**
 * 商品参数类型策略。
 * 负责检查解析商品参数值。
 * @author aha
 * @since 2020/10/27
 */
public interface ProductParamValueStrategy {
    /**
     * 从json类型的参数值中解析获取文本类型的参数值。
     * 如果解析失败则返回null。
     *
     * @param value json类型的参数值
     * @return 文本类型的参数值
     */
    String getTextValue(String value);
}
