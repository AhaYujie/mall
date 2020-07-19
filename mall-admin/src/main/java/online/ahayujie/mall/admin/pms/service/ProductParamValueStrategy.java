package online.ahayujie.mall.admin.pms.service;

import online.ahayujie.mall.admin.pms.exception.IllegalProductParamException;

/**
 * 商品参数类型策略。
 * 负责检查解析商品参数
 * @author aha
 * @since 2020/7/15
 */
public interface ProductParamValueStrategy {
    /**
     * 检查商品参数值合法性
     * @param value 商品参数值
     * @throws IllegalProductParamException 商品参数值不合法
     */
    void validate(String value) throws IllegalProductParamException;
}
